package whatexe.dungeoncrawler.entities.motionsupport;

import javafx.geometry.Rectangle2D;
import whatexe.dungeoncrawler.entities.Entity;

import java.util.*;

/**
 * Many thanks to
 * <a href=
 * "https://www.azurefromthetrenches.com/introductory-guide-to-aabb-tree-collision-detection/">
 * James at Azure From The Trenches
 * </a>
 * and
 * <a href=
 * "https://www.gamasutra.com/view/feature/131790/simple_intersection_tests_for_games.php?page=3">
 * Miguel Gomez on Gamasutra
 * </a>
 * for the C++ code for this to work!
 */
public class AABbTree {

    private static final double FATTENING_FACTOR = 40;

    private final Map<Entity, Integer> entityNodeIndexMap;
    private AABbNode[] nodes;
    private int rootNodeIndex;
    private int size;
    private int nextFreeNodeIndex;
    private int capacity;
    private int growthSize;

    public AABbTree(int initialSize) {
        rootNodeIndex = -1;
        size = 0;
        nextFreeNodeIndex = 0;
        capacity = initialSize;
        growthSize = initialSize;
        entityNodeIndexMap = new IdentityHashMap<>();

        nodes = new AABbNode[capacity];
        for (int i = 0; i < capacity - 1; i++) {
            AABbNode newNode = new AABbNode();
            newNode.nextNodeIndex = i + 1;
            nodes[i] = newNode;
        }
        nodes[capacity - 1] = new AABbNode();
    }

    private int allocateNode() {
        if (nextFreeNodeIndex == -1) {
            assert size == capacity;

            capacity += growthSize;
            AABbNode[] temp = new AABbNode[capacity];
            System.arraycopy(nodes, 0, temp, 0, size);
            nodes = temp;

            for (int i = size; i < capacity - 1; i++) {
                AABbNode newNode = new AABbNode();
                newNode.nextNodeIndex = i + 1;
                nodes[i] = newNode;
            }
            nodes[capacity - 1] = new AABbNode();
            nextFreeNodeIndex = size;
        }

        int index = nextFreeNodeIndex;
        AABbNode allocatedNode = nodes[index];
        nextFreeNodeIndex = allocatedNode.nextNodeIndex;
        size++;

        return index;
    }

    private void deallocateNode(int nodeIndex) {
        AABbNode deallocatedNode = nodes[nodeIndex];
        deallocatedNode.nextNodeIndex = nextFreeNodeIndex;
        nextFreeNodeIndex = nodeIndex;
        size--;
    }

    private void insertLeaf(int leafNodeIndex) {
        assert nodes[leafNodeIndex].parentNodeIndex == -1;
        assert nodes[leafNodeIndex].leftNodeIndex == -1;
        assert nodes[leafNodeIndex].rightNodeIndex == -1;

        if (rootNodeIndex == -1) {
            rootNodeIndex = leafNodeIndex;
            return;
        }

        // search for the best place to put the leaf into the tree, using surface area and depth
        // as balancing heuristics
        int treeNodeIndex = rootNodeIndex;
        AABbNode leafNode = nodes[leafNodeIndex];
        while (!nodes[treeNodeIndex].isLeaf()) {
            AABbNode treeNode = nodes[treeNodeIndex];
            int leftNodeIndex = treeNode.leftNodeIndex;
            int rightNodeIndex = treeNode.rightNodeIndex;
            AABbNode leftNode = nodes[leftNodeIndex];
            AABbNode rightNode = nodes[rightNodeIndex];

            AABB combinedBounds = treeNode.bounds.merge(leafNode.bounds);
            double newParentNodeCost = 2 * combinedBounds.surfaceArea;
            double minimumPushDownCost =
                    2 * (combinedBounds.surfaceArea - treeNode.bounds.surfaceArea);


            double costLeft = minimumPushDownCost;
            double costRight = minimumPushDownCost;

            AABB newLeftBounds = leafNode.bounds.merge(leftNode.bounds);
            if (leftNode.isLeaf()) {
                costLeft += newLeftBounds.surfaceArea;
            } else {
                costLeft += newLeftBounds.surfaceArea - leftNode.bounds.surfaceArea;
            }

            AABB newRightBounds = leafNode.bounds.merge(rightNode.bounds);
            if (rightNode.isLeaf()) {
                costRight += newRightBounds.surfaceArea;
            } else {
                costRight += newRightBounds.surfaceArea - rightNode.bounds.surfaceArea;
            }

            // if the cost of creating a new parent is less than descending, create the new node
            // here
            if (newParentNodeCost < costLeft && newParentNodeCost < costRight) {
                break;
            }

            if (costRight < costLeft) {
                treeNodeIndex = rightNodeIndex;
            } else {
                treeNodeIndex = leftNodeIndex;
            }
        }

        // new sibling is the treeNodeIndex, create the new parent and attachments
        int leafSiblingIndex = treeNodeIndex;
        AABbNode leafSibling = nodes[leafSiblingIndex];
        int oldParentIndex = leafSibling.parentNodeIndex;
        int newParentIndex = allocateNode();
        AABbNode newParent = nodes[newParentIndex];
        newParent.parentNodeIndex = oldParentIndex;
        newParent.bounds = leafNode.bounds.merge(leafSibling.bounds);
        newParent.leftNodeIndex = leafSiblingIndex;
        newParent.rightNodeIndex = leafNodeIndex;
        leafNode.parentNodeIndex = newParentIndex;
        leafSibling.parentNodeIndex = newParentIndex;

        if (oldParentIndex == -1) {
            // If the old parent was the root, this is the new root
            rootNodeIndex = newParentIndex;
        } else {
            // we need to point the old parent to the new parent
            AABbNode oldParent = nodes[oldParentIndex];
            if (oldParent.leftNodeIndex == leafSiblingIndex) {
                oldParent.leftNodeIndex = newParentIndex;
            } else {
                oldParent.rightNodeIndex = newParentIndex;
            }
        }

        // finally walk back up, fixing heights and areas
        fixUpwardsTree(leafNode.parentNodeIndex);
    }

    private void removeLeaf(int leafNodeIndex) {
        // if the leaf is the root then we can just clear the root pointer and return
        if (leafNodeIndex == rootNodeIndex) {
            rootNodeIndex = -1;
            return;
        }

        AABbNode leafNode = nodes[leafNodeIndex];
        int parentNodeIndex = leafNode.parentNodeIndex;
        AABbNode parentNode = nodes[parentNodeIndex];
        int grandParentNodeIndex = parentNode.parentNodeIndex;
        int siblingNodeIndex =
                parentNode.leftNodeIndex == leafNodeIndex ? parentNode.rightNodeIndex
                        : parentNode.leftNodeIndex;
        assert siblingNodeIndex != -1; // we must have a sibling
        AABbNode siblingNode = nodes[siblingNodeIndex];

        if (grandParentNodeIndex != -1) {
            // if we have a grand parent (i.e. the parent is not the root) then destroy the
            // parent and connect the sibling to the grandparent in its
            // place
            AABbNode grandParentNode = nodes[grandParentNodeIndex];
            if (grandParentNode.leftNodeIndex == parentNodeIndex) {
                grandParentNode.leftNodeIndex = siblingNodeIndex;
            } else {
                grandParentNode.rightNodeIndex = siblingNodeIndex;
            }
            siblingNode.parentNodeIndex = grandParentNodeIndex;
            deallocateNode(parentNodeIndex);

            fixUpwardsTree(grandParentNodeIndex);
        } else {
            // if we have no grandparent then the parent is the root and so our sibling becomes
            // the root and has it's parent removed
            rootNodeIndex = siblingNodeIndex;
            siblingNode.parentNodeIndex = -1;
            deallocateNode(parentNodeIndex);
        }

        leafNode.parentNodeIndex = -1;
    }

    private void updateLeaf(int leafIndex, AABB newBounds) {
        AABbNode node = nodes[leafIndex];

        if (node.bounds.contains(newBounds)) {
            return;
        }

        removeLeaf(leafIndex);
        node.bounds = newBounds;
        insertLeaf(leafIndex);
    }

    private void fixUpwardsTree(int treeNodeIndex) {
        while (treeNodeIndex != -1) {
            AABbNode treeNode = nodes[treeNodeIndex];

            // every node should be a parent
            assert treeNode.leftNodeIndex != -1 && treeNode.rightNodeIndex != -1;

            // fix height and area
            AABbNode leftNode = nodes[treeNode.leftNodeIndex];
            AABbNode rightNode = nodes[treeNode.rightNodeIndex];
            treeNode.bounds = leftNode.bounds.merge(rightNode.bounds);

            // expand by 0.5 * FATTENING_FACTOR px on all sides
            treeNode.bounds = treeNode.bounds.expandedBy(FATTENING_FACTOR, FATTENING_FACTOR);

            treeNodeIndex = treeNode.parentNodeIndex;
        }
    }

    public void insert(Entity entity) {
        int nodeIndex = allocateNode();
        AABbNode node = nodes[nodeIndex];

        node.bounds = new AABB(entity.getHitbox());
        node.entity = entity;

        insertLeaf(nodeIndex);
        entityNodeIndexMap.put(entity, nodeIndex);
    }

    public void remove(Entity entity) {
        if (!entityNodeIndexMap.containsKey(entity)) {
            return;
        }
        int nodeIndex = entityNodeIndexMap.remove(entity);
        removeLeaf(nodeIndex);
        deallocateNode(nodeIndex);
    }

    public void update(Entity entity) {
        int nodeIndex = entityNodeIndexMap.get(entity);
        updateLeaf(nodeIndex, new AABB(entity.getHitbox()));
    }

    public List<Entity> queryOverlaps(Entity entity) {
        return queryOverlaps(entity, null);
    }

    public List<Entity> queryOverlaps(Entity entity, List<? extends Entity> filter) {
        LinkedList<Entity> overlaps = new LinkedList<>();
        Stack<Integer> checkStack = new Stack<>();
        AABB testBounds = new AABB(entity.getHitbox());

        checkStack.push(rootNodeIndex);
        while (!checkStack.isEmpty()) {
            int nodeIndex = checkStack.pop();

            if (nodeIndex == -1) {
                continue;
            }

            AABbNode node = nodes[nodeIndex];
            if (node.bounds.overlaps(testBounds)) {
                if (node.isLeaf() && node.entity != entity) {
                    if (node.entity instanceof BoundaryRectangle
                            || filter == null
                            || filter.contains(node.entity)) {
                        overlaps.push(node.entity);
                    }
                } else {
                    checkStack.push(node.leftNodeIndex);
                    checkStack.push(node.rightNodeIndex);
                }
            }
        }

        return overlaps;
    }

    public void addAll(Collection<? extends Entity> toAdd) {
        for (Entity entity : toAdd) {
            insert(entity);
        }
    }

    public void removeAll(Collection<? extends Entity> toRemove) {
        for (Entity entity : toRemove) {
            remove(entity);
        }
    }

    public ConstrainedVelocityData getConstrainedVelocity(Entity entity,
                                                          Vector attemptVelocity) {
        List<? extends Entity> collisionFilter =
                entity.getBehaviorSet().getCollisionBehavior().getPossibleCollisionTargets();
        // If already inside an entity
        List<Entity> currentOverlaps = queryOverlaps(entity, collisionFilter);
        if (currentOverlaps.size() > 0) {
            // System.out.printf("Inside! %s%n", entity.getHitbox());
            return new ConstrainedVelocityData(attemptVelocity, currentOverlaps);
        }
        List<Entity> collidedWith = new ArrayList<>();
        Vector constrainedVelocity = new Vector(0, 0);

        AABB entityBounds = nodes[entityNodeIndexMap.get(entity)].bounds;

        List<Entity> entitiesInSweep = checkSweptAABB(entity, attemptVelocity, collisionFilter);

        // If not going to collide with anything
        if (entitiesInSweep.size() == 0) {
            return new ConstrainedVelocityData(attemptVelocity, List.of());
        }

        int attempt = 0;

        while (!attemptVelocity.isZero() && entitiesInSweep.size() > 0) {
            Entity closestEntity = entitiesInSweep.get(0);
            double closestTime = 1;
            AABB closestBounds = nodes[entityNodeIndexMap.get(closestEntity)].bounds;

            for (Entity collidedAgainst : entitiesInSweep) {
                if (!collisionFilter.contains(collidedAgainst)
                        && !(collidedAgainst instanceof BoundaryRectangle)) {
                    continue;
                }
                AABB collidedAABB = nodes[entityNodeIndexMap.get(collidedAgainst)].bounds;
                Vector relativeVelocity = attemptVelocity.scaledBy(-1);

                Vector u0Vector = new Vector(0, 0);  // normalized axis-wise collision start
                Vector u1Vector = new Vector(1, 1);  // normalized axis-wise collision end

                if (entityBounds.maxX < collidedAABB.minX && relativeVelocity.get(0) < 0) {
                    u0Vector.getV()[0] = (entityBounds.maxX - collidedAABB.minX)
                            / relativeVelocity.get(0);
                } else if (collidedAABB.maxX < entityBounds.minX && relativeVelocity.get(0) > 0) {
                    u0Vector.getV()[0] = (entityBounds.minX - collidedAABB.maxX)
                            / relativeVelocity.get(0);
                }
                if (collidedAABB.maxX > entityBounds.minX && relativeVelocity.get(0) < 0) {
                    u1Vector.getV()[0] = (entityBounds.minX - collidedAABB.maxX)
                            / relativeVelocity.get(0);
                } else if (entityBounds.maxX > collidedAABB.minX && relativeVelocity.get(0) > 0) {
                    u1Vector.getV()[0] = (entityBounds.maxX - collidedAABB.minX)
                            / relativeVelocity.get(0);
                }

                if (entityBounds.maxY < collidedAABB.minY && relativeVelocity.get(1) < 0) {
                    u0Vector.getV()[1] = (entityBounds.maxY - collidedAABB.minY)
                            / relativeVelocity.get(1);
                } else if (collidedAABB.maxY < entityBounds.minY && relativeVelocity.get(1) > 0) {
                    u0Vector.getV()[1] = (entityBounds.minY - collidedAABB.maxY)
                            / relativeVelocity.get(1);
                }
                if (collidedAABB.maxY > entityBounds.minY && relativeVelocity.get(1) < 0) {
                    u1Vector.getV()[1] = (entityBounds.minY - collidedAABB.maxY)
                            / relativeVelocity.get(1);
                } else if (entityBounds.maxY > collidedAABB.minY && relativeVelocity.get(1) > 0) {
                    u1Vector.getV()[1] = (entityBounds.maxY - collidedAABB.minY)
                            / relativeVelocity.get(1);
                }

                double u0 = Math.max(u0Vector.get(0), u0Vector.get(1));
                double u1 = Math.min(u1Vector.get(0), u1Vector.get(1));

                // collision only possible if overlap start occurs before overlap end
                if (u0 > u1) {
                    continue;
                }

                if (u0 < closestTime) {
                    closestTime = u0;
                    closestEntity = collidedAgainst;
                    closestBounds = collidedAABB;
                }
            }
            collidedWith.add(closestEntity);
            constrainedVelocity = constrainedVelocity.plus(attemptVelocity.scaledBy(closestTime));

            Vector[] entityEdgeMidpoints =
                    entityBounds.shiftedBy(constrainedVelocity).edgeMidpoints();
            Vector[] targetEdgeMidpoints = closestBounds.edgeMidpoints();
            // If either of these vectors are nearly horizontal, moving entity is on top/bottom of
            // colliding entity
            Vector topEntityToBottomTarget =
                    targetEdgeMidpoints[3].minus(entityEdgeMidpoints[0]).unit();
            Vector bottomEntityToTopTarget =
                    targetEdgeMidpoints[0].minus(entityEdgeMidpoints[3]).unit();
            Vector e1 = new Vector(0, 1);
            boolean topOrBottom =
                    Math.abs(topEntityToBottomTarget.dot(e1)) < 1e-6
                            || Math.abs(bottomEntityToTopTarget.dot(e1)) < 1e-6;

            // If either of these vectors are nearly vertical, moving entity is on left/right of
            // colliding entity
            Vector leftEntityToRightTarget =
                    targetEdgeMidpoints[2].minus(entityEdgeMidpoints[1]).unit();
            Vector rightEntityToLeftTarget =
                    targetEdgeMidpoints[1].minus(entityEdgeMidpoints[2]).unit();
            Vector e0 = new Vector(1, 0);
            boolean leftOrRight =
                    Math.abs(leftEntityToRightTarget.dot(e0)) < 1e-6
                            || Math.abs(rightEntityToLeftTarget.dot(e0)) < 1e-6;

            Vector remainder = new Vector(0, 0);
            if (topOrBottom && !leftOrRight) {
                remainder = Line.projectVector(e0, attemptVelocity).scaledBy(1 - closestTime);
            } else if (leftOrRight && !topOrBottom) {
                remainder = Line.projectVector(e1, attemptVelocity).scaledBy(1 - closestTime);
            }

            attemptVelocity = remainder;
            entitiesInSweep = checkSweptAABB(entity,
                                             attemptVelocity.plus(constrainedVelocity),
                                             collisionFilter);

            // if (entitiesInSweep.size() != 0) {
            //     System.out.println(attempt + " " + entitiesInSweep);
            // }
            //
            // System.out.printf("%d: %s %s %s %s%n",
            //                   attempt,
            //                   constrainedVelocity,
            //                   attemptVelocity,
            //                   entityBounds,
            //                   entitiesInSweep);
            attempt++;
        }

        return new ConstrainedVelocityData(attemptVelocity.plus(constrainedVelocity),
                                           collidedWith);
    }

    private List<Entity> checkSweptAABB(Entity entity, Vector sweep) {
        return checkSweptAABB(entity, sweep, null);
    }

    private List<Entity> checkSweptAABB(Entity entity,
                                        Vector sweep,
                                        List<? extends Entity> filter) {
        AABB currentBounds = nodes[entityNodeIndexMap.get(entity)].bounds;
        AABB sweepBounds = currentBounds.merge(currentBounds.shiftedBy(sweep));
        List<Entity> allInSweep = entitiesInBounds(sweepBounds, filter);
        allInSweep.remove(entity);

        return allInSweep;
    }

    private List<Entity> entitiesInBounds(AABB specificBounds, List<? extends Entity> filter) {
        LinkedList<Entity> inBounds = new LinkedList<>();
        Stack<Integer> checkStack = new Stack<>();

        checkStack.push(rootNodeIndex);
        while (!checkStack.isEmpty()) {
            int nodeIndex = checkStack.pop();

            if (nodeIndex == -1) {
                continue;
            }

            AABbNode node = nodes[nodeIndex];
            if (node.bounds.overlaps(specificBounds)) {
                if (node.isLeaf()) {
                    if (node.entity instanceof BoundaryRectangle
                            || filter == null
                            || filter.contains(node.entity)) {
                        inBounds.push(node.entity);
                    }
                } else {
                    checkStack.push(node.leftNodeIndex);
                    checkStack.push(node.rightNodeIndex);
                }
            }
        }

        return inBounds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AABBTree{\n");
        sb.append("entityNodeIndexMap=").append(entityNodeIndexMap);
        sb.append(",\nnodes=\n").append(Arrays.toString(nodes));
        sb.append(",\nrootNodeIndex=").append(rootNodeIndex);
        sb.append(",\nsize=").append(size);
        sb.append(",\nnextFreeNodeIndex=").append(nextFreeNodeIndex);
        sb.append(",\ncapacity=").append(capacity);
        sb.append(",\ngrowthSize=").append(growthSize);
        sb.append("\n}");
        return sb.toString();
    }

    private static class AABB {
        private final double minX;
        private final double maxX;
        private final double minY;
        private final double maxY;
        private final double surfaceArea;

        public AABB(double minX, double maxX, double minY, double maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            surfaceArea = getWidth() * getHeight();
        }

        public AABB(Rectangle2D bounds) {
            this(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
        }

        private boolean overlaps(AABB other) {
            return maxX > other.minX
                    && minX < other.maxX
                    && maxY > other.minY
                    && minY < other.maxY;
        }

        private boolean contains(AABB other) {
            return other.minX >= minX
                    && other.maxX <= maxX
                    && other.minY >= minY
                    && other.maxY <= maxY;
        }

        private AABB merge(AABB other) {
            return new AABB(Math.min(minX, other.minX),
                            Math.max(maxX, other.maxX),
                            Math.min(minY, other.minY),
                            Math.max(maxY, other.maxY));
        }

        private AABB grownBy(double scale) {
            double currentWidth = getWidth();
            double currentHeight = getHeight();
            double newWidth = currentWidth * scale;
            double newHeight = currentHeight * scale;
            double dx = newWidth - currentWidth;
            double dy = newHeight - currentHeight;

            return expandedBy(dx, dy);
        }

        /**
         * @param dx how much wider the new AABB should be from this one
         * @param dy how much taller the new AABB should be from this one
         * @return a new AABB, expanded outwards from the center of this one
         */
        private AABB expandedBy(double dx, double dy) {
            return new AABB(minX - dx / 2,
                            maxX + dx / 2,
                            minY - dy / 2,
                            maxY + dy / 2);
        }

        private AABB shiftedBy(Vector vector) {
            return new AABB(minX + vector.get(0),
                            maxX + vector.get(0),
                            minY + vector.get(1),
                            maxY + vector.get(1));
        }

        private double getWidth() {
            return maxX - minX;
        }

        private double getHeight() {
            return maxY - minY;
        }

        private double getCenterX() {
            return (minX + maxX) / 2;
        }

        private double getCenterY() {
            return (minY + maxY) / 2;
        }

        private Vector getCenter() {
            return new Vector(getCenterX(), getCenterY());
        }

        private Vector getHalfExtents() {
            return new Vector(getWidth() / 2, getHeight() / 2);
        }

        /**
         * @return {top edge midpoint, left edge midpoint, right edge midpoint, bottom edge
         * midpoint}
         */
        private Vector[] edgeMidpoints() {
            Vector topMidpoint = getCenter().plus(
                    new Vector(0, getHalfExtents().get(1)));
            Vector bottomMidpoint = getCenter().plus(
                    new Vector(0, -getHalfExtents().get(1)));
            Vector leftMidpoint = getCenter().plus(
                    new Vector(-getHalfExtents().get(0), 0));
            Vector rightMidpoint = getCenter().plus(
                    new Vector(getHalfExtents().get(0), 0));

            return new Vector[]{topMidpoint, leftMidpoint, rightMidpoint, bottomMidpoint};
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("AABB{");
            sb.append("minX=").append(minX);
            sb.append(", maxX=").append(maxX);
            sb.append(", minY=").append(minY);
            sb.append(", maxY=").append(maxY);
            sb.append(", surfaceArea=").append(surfaceArea);
            sb.append(", width=").append(getWidth());
            sb.append(", height=").append(getHeight());
            sb.append('}');
            return sb.toString();
        }
    }

    private static class AABbNode {
        private Entity entity;
        private AABB bounds;

        // Tree links
        private int parentNodeIndex;
        private int leftNodeIndex;
        private int rightNodeIndex;

        // LinkedList links
        private int nextNodeIndex;

        private AABbNode() {
            parentNodeIndex = -1;
            leftNodeIndex = -1;
            rightNodeIndex = -1;
            nextNodeIndex = -1;
        }

        private boolean isLeaf() {
            return leftNodeIndex == -1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("\nAABBNode{");
            sb.append("\tentity=").append(entity);
            sb.append(",\n\tbounds=").append(bounds);
            sb.append(",\n\tparentNodeIndex=").append(parentNodeIndex);
            sb.append(",\n\tleftNodeIndex=").append(leftNodeIndex);
            sb.append(",\n\trightNodeIndex=").append(rightNodeIndex);
            sb.append(",\n\tnextNodeIndex=").append(nextNodeIndex);
            sb.append("\n\t}");
            return sb.toString();
        }
    }

    public static class ConstrainedVelocityData {
        private final Vector constrainedVelocity;
        private final List<Entity> collidedWith;

        public ConstrainedVelocityData(Vector constrainedVelocity,
                                       List<Entity> collidedWith) {
            this.constrainedVelocity = constrainedVelocity;
            this.collidedWith = collidedWith;
        }

        public Vector getConstrainedVelocity() {
            return constrainedVelocity;
        }

        public List<Entity> getCollidedWith() {
            return collidedWith;
        }
    }
}
