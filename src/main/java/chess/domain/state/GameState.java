package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Position;

public interface GameState {

    boolean isEnd();

    GameState terminate();

    GameState move(Position start, Position target);

    String findTurn();

    Winner findWinner();

    Board getBoard();
}
