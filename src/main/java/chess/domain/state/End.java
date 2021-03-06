package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Position;

public abstract class End extends GameStarted {

    public End(Board board) {
        super(board);
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public GameState terminate() {
        throw new IllegalStateException("이미 게임이 종료되었습니다.");
    }

    @Override
    public GameState move(Position start, Position target) {
        throw new IllegalStateException("게임이 끝나서 말을 움직일 수 없습니다.");
    }

    @Override
    public String findTurn() {
        throw new IllegalStateException("이미 게임이 종료되었습니다.");
    }
}
