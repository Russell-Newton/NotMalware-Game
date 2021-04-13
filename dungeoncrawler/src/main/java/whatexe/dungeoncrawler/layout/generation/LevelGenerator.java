package whatexe.dungeoncrawler.layout.generation;

import whatexe.dungeoncrawler.layout.Level;

public interface LevelGenerator {

    Level generate(int depth);

    Level generate();

}
