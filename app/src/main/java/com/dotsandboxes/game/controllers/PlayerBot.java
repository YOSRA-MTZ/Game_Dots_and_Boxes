package com.dotsandboxes.game.controllers;



import com.dotsandboxes.game.models.Board;
import com.dotsandboxes.game.models.Edge;
import com.dotsandboxes.game.models.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import timber.log.Timber;

/**
 * This is computer-bot player
 */
public class PlayerBot {
    private Game game;

    public PlayerBot(Game game) {
        this.game = game;
    }

    public Edge getNextMove() {

        // get a box completion move
        ArrayList<Edge> completionMoves = getCompletionMoves(game.getGameTree(), game.getBoard());

        // return the completion move that will be most profitable if such exists
        if (completionMoves.size() > 0) {
            Collections.shuffle(completionMoves);
            return completionMoves.get(0);
        }
        // otherwise get a random available move
        else {
            int minOpponentScoreAfterMove = Integer.MAX_VALUE;
            int opponentScore;
            Edge nextMove = null;

            ArrayList<Edge> availableMoves = game.getGameTree().getAvailableEdges();
            // try every move and see how many points can the opponent make afterwards
            for (Edge moveToMake : availableMoves) {
                // work with  a copy of the board so that it will not affect the real game board
                Board boardCopy = game.getBoard().getCopy();
                Graph gameTreeCopy = game.getGameTree().getCopy();
                ArrayList<Edge> opponentCompletionMoves;

                // place the bot future move
                boardCopy.setLineForDots(moveToMake.getDotStart(), moveToMake.getDotEnd(), Game.Player.PLAYER2);

                // record the move in the game tree copy
                gameTreeCopy.addEdge(moveToMake);


                // start making all moves in favour of the opponent (human mService) and see which move will lead to him making the least points
                opponentScore = 0;
                opponentCompletionMoves = getCompletionMoves(gameTreeCopy, boardCopy);

                Graph gameTreeOpponent = gameTreeCopy.getCopy();
                Board boardOpponent = boardCopy.getCopy();

                while (opponentCompletionMoves.size() > 0) {
                    opponentScore += opponentCompletionMoves.size();


                    for (Edge opponentMoveToMake : opponentCompletionMoves) {
                        boardOpponent.setLineForDots(opponentMoveToMake.getDotStart(), opponentMoveToMake.getDotEnd(), Game.Player.PLAYER1);
                        gameTreeOpponent.addEdge(opponentMoveToMake);
                        Timber.e(opponentMoveToMake.toString());
                    }

                    opponentCompletionMoves = getCompletionMoves(gameTreeOpponent, boardOpponent);
                }

                if (opponentScore < minOpponentScoreAfterMove) {
                    minOpponentScoreAfterMove = opponentScore;
                    nextMove = moveToMake;
                }
            }

            if (nextMove != null) {
                return nextMove;
            }
            else {
                Collections.shuffle(availableMoves);
                return availableMoves.get(0);
            }
        }
    }

    /**
     * The method searches by a greedy algorithm for a box with a fourth wall missing.
     * @return all edges that will complete a box
     */
    private ArrayList<Edge> getCompletionMoves(Graph gameTree, Board board) {

        ArrayList<Edge> completionMoves = new ArrayList<>();
        HashMap<String, Edge> madeMoves = gameTree.getEdges();
        ArrayList<Edge> availableMoves = gameTree.getAvailableEdges();

        for (Edge moveToMake : availableMoves) {
            if (!madeMoves.containsKey(moveToMake.getKey())) {
                Board tempBoard = board.getCopy();
                int scoreBefore = tempBoard.getScore(Game.Player.PLAYER2);
                tempBoard.setLineForDots(moveToMake.getDotStart(), moveToMake.getDotEnd(), Game.Player.PLAYER2);
                int scoreAfter = tempBoard.getScore(Game.Player.PLAYER2);

                if (scoreBefore < scoreAfter) {
                    completionMoves.add(moveToMake);
                }
            }
        }

        return completionMoves;
    }



}