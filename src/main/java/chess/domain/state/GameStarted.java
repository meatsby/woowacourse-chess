package chess.domain.state;

import chess.domain.board.Rank;
import java.util.Map;

public abstract class GameStarted implements GameState {

    protected final Map<Integer, Rank> ranks;

    public GameStarted(Map<Integer, Rank> ranks) {
        this.ranks = ranks;
    }

    public Rank getRank(int rankLine) {
        return ranks.get(rankLine);
    }
}
