package com.bayviewglen.zork;

import java.util.Scanner;

public class Poker {
	
	/*
	 * Description: User plays 3-card poker against a dealer. Asked to place an ante
	 * wager, and then given the option for pair plus. User is asked if they want to
	 * play or fold, and if they play, the best hand wins. Pair plus wager is
	 * determined apart from playing, according to the hand of the player, and only
	 * requires the user to not fold.
	 */

	public static final int jackValue = 11;
	public static final int queenValue = 12;
	public static final int kingValue = 13;
	public static final int aceValue = 14;

	public static final int NUM_SUITS = 4;
	public static final int NUM_FACES = 13;
	public static final int MIN_BET = 50;
	public static final int MAX_BET = 100;

	public static final int STRAIGHT_FLUSH_PAIR = 40;
	public static final int THREE_OF_A_KIND_PAIR = 30;
	public static final int STRAIGHT_PAIR = 6;
	public static final int FLUSH_PAIR = 3;
	public static final int PAIR_PAIR = 1;
	public static final int HIGH_CARD_PAIR = 0;

	public static final int STRAIGHT_FLUSH = 500;
	public static final int THREE_OF_A_KIND = 400;
	public static final int STRAIGHT = 300;
	public static final int FLUSH = 200;
	public static final int PAIR = 100;
	public static final int HIGH_CARD = 0;

	public static void main(String[] args) {

		int playerWallet = 500;
		int anteWager = 0;
		int pairPlusWager = 0;
		int playWager = 0;
		int playerHandValue;
		int dealerHandValue;
		String playerHand = "";
		String dealerHand = "";
		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to 3-Card poker!!!\n");

		boolean isGameOver = false;
		while (!isGameOver) {
			anteWager = 0;
			pairPlusWager = 0;
			playWager = 0;
			playerHandValue = 0;
			dealerHandValue = 0;
			playerHand = "";
			dealerHand = "";

			anteWager += getValidAnte(in, playerWallet);
			playerWallet -= anteWager;

			if (isPairPlus(in, playerWallet))
				pairPlusWager += getValidPairPlus(in, playerWallet);
			playerWallet += anteWager;
			/*
			 * The reason for subtracting and adding the ante wager here is because the pair
			 * plus wager checks wallet to see if money is left, and actual wagers are
			 * subtracted later. Bypass was to take it out for only pairPlus and then put it
			 * back.
			 */

			// Creation of player's Hand
			for (int i = 0; i < 3; i++) {
				Boolean isDuplicate = false;
				while (!isDuplicate) {
					String card = getCard();
					if (isUnique(playerHand, card)) {
						playerHand += card + " ";
						isDuplicate = true;
					}
				}
			}
			// Creation of dealer's hand
			for (int i = 0; i < 3; i++) {
				Boolean isDuplicate = false;
				while (!isDuplicate) {
					String card = getCard();
					if (isUnique(dealerHand, playerHand, card)) {
						dealerHand += card + " ";
						isDuplicate = true;
					}
				}
			}

			System.out.println("Dealer: XX XX XX");
			/* Dealer's hand is hidden but already made */

			System.out.println("Player: " + playerHand);

			if (isGonnaPlay(in)) {
				playWager += anteWager;
				/* play wager is just the anteWager again */

				dealerHandValue += handType(dealerHand) + getBestCard(dealerHand);
				playerHandValue += handType(playerHand) + getBestCard(playerHand);
				/*
				 * Above uses handType() method and getBestCard() to find total points for each
				 * hand.
				 */
				System.out.println("Dealer: " + dealerHand);
				System.out.println("Player: " + playerHand);

				if (isDealerValid(dealerHand) && playerHandValue > dealerHandValue) {
					playerWallet += playWager + anteWager;
					System.out.println("You win, damn");

				} else if (isDealerValid(dealerHand) && dealerHandValue > playerHandValue) {
					playerWallet -= playWager + anteWager;
					System.out.println("You lose, get destroyed!");

				} else if (isDealerValid(dealerHand) && dealerHandValue == playerHandValue
						&& getBestCard(dealerHand) == getBestCard(playerHand)) {
					System.out.println("Nobody wins...better luck next time pal.");
					playerWallet += playWager;
					playerWallet -= anteWager;

				} else {
					/* happens when the dealer's hand is not valid */
					playerWallet += playWager;
					playerWallet -= anteWager;
					System.out.println("Nobody wins...");
				}

				int pairPlusWinnings = 0;

				if (handType(playerHand) == STRAIGHT_FLUSH)
					pairPlusWinnings = STRAIGHT_FLUSH_PAIR * pairPlusWager;
				else if (handType(playerHand) == THREE_OF_A_KIND)
					pairPlusWinnings = THREE_OF_A_KIND_PAIR * pairPlusWager;
				else if (handType(playerHand) == STRAIGHT)
					pairPlusWinnings = STRAIGHT_PAIR * pairPlusWager;
				else if (handType(playerHand) == FLUSH)
					pairPlusWinnings = FLUSH_PAIR * pairPlusWager;
				else if (handType(playerHand) == PAIR)
					pairPlusWinnings = PAIR_PAIR * pairPlusWager;
				else
					pairPlusWinnings = HIGH_CARD_PAIR;
				/*
				 * Since my variables for each special case award points, I made new ones for
				 * the pairPlus returns. The code above multiplies the wager by the number
				 * returned using handType() method.
				 */
				if (pairPlusWinnings > HIGH_CARD_PAIR) {
					playerWallet += pairPlusWinnings;
					System.out.println("+ $" + pairPlusWinnings + " from your pair plus wager!!! GOSH DARN IT");
				} else
					playerWallet -= pairPlusWager;

			} else {
				playerWallet -= anteWager + pairPlusWager;
				System.out.println("Better luck next time! HAHAHA");
			}

			isGameOver = playAgain(in, playerWallet);

		}
		//THIS IS WHERE YOU EXIT THE GAME
		System.out.println(
				"\nThanks for playing! \n\nHope you know when hold 'em, \nknow when to fold 'em, \nknow when to walk away, \nknow when to run.");

		in.close();

	}

	public static int getBestCard(String hand) {
		/*
		 * This method receives a hand of cards, separates it with the getCardFromHand()
		 * method, and returns the value that's the highest
		 */
		String card1 = getCardFromHand(hand, 1);
		String card2 = getCardFromHand(hand, 2);
		String card3 = getCardFromHand(hand, 3);

		int card1Value = getCardValue(card1);
		int card2Value = getCardValue(card2);
		int card3Value = getCardValue(card3);

		if (card1Value > card2Value && card1Value > card3Value)
			return card1Value;
		else if (card2Value > card1Value && card2Value > card3Value)
			return card2Value;
		else
			return card3Value;
	}

	public static boolean isDealerValid(String dealerHand) {
		/*
		 * This method uses the dealerHand string, along with the handType() method to
		 * determine whether or not the dealer's hand is jack-high or greater.
		 */
		if (handType(dealerHand) == HIGH_CARD && dealerHand.indexOf("Q") == -1 && dealerHand.indexOf("K") == -1
				&& dealerHand.indexOf("A") == -1)
			return false;
		else
			return true;
	}

	public static boolean isGonnaPlay(Scanner in) {
		/*
		 * Prompts the user if they want to play or fold their hand. Returns boolean
		 * since they can either play or not.
		 */
		System.out.print("Do you want to play this hand? (Y/N)");

		boolean isValidInput = false;
		while (!isValidInput) {
			String temp = in.nextLine().toLowerCase();
			if (temp.equals("y") || temp.equals("yes"))
				return true;
			else if (temp.equals("n") || temp.equals("no"))
				return false;
			else
				System.out.println("Do you want to play this hand? (Y/N)");
		}
		return false;
	}

	public static boolean isPairPlus(Scanner in, int playerWallet) {
		/*
		 * Prompts the user to either play a pair plus wager or not. Returns boolean and
		 * checks with wallet before asking.
		 */
		if (playerWallet < MIN_BET)
			return false;

		System.out.print("Do you want to make a pair plus wager? (Y/N)");

		boolean isValidInput = false;
		while (!isValidInput) {
			String temp = in.nextLine().toLowerCase();
			if (temp.equals("y") || temp.equals("yes"))
				return true;
			else if (temp.equals("n") || temp.equals("no"))
				return false;
			else
				System.out.println("Do you want to make a pair plus wager? (Y/N)");
		}
		return false;
	}

	public static boolean playAgain(Scanner in, int playerWallet) {
		/*
		 * Asks the user if they want to play again or not. Checks with wallet before
		 * asking and returns boolean. It is <= to MAX_BET since ante is 50 and then
		 * play wager is also 50.
		 */
		if (playerWallet <= MAX_BET)
			return true;

		System.out.println("\nPlay again (Y/N)? you have $" + playerWallet + " left.");
		boolean isValidInput = false;
		while (!isValidInput) {
			String temp = in.nextLine().toLowerCase();
			if (temp.equals("y") || temp.equals("yes"))
				return false;
			else if (temp.equals("n") || temp.equals("no"))
				return true;
			else
				System.out.println("Play again (Y/N)? ");
		}
		return true;

	}

	public static int handType(String hand) {
		/*
		 * First breaks up the inputed hand into the 3 cards using getCardFromHand()
		 * method. Then uses those cards to check for special cases such as pair or
		 * flush.
		 */
		String card1 = getCardFromHand(hand, 1);
		String card2 = getCardFromHand(hand, 2);
		String card3 = getCardFromHand(hand, 3);

		/*
		 * Returns points according to the type of hand.
		 */
		if (isStraightFlush(card1, card2, card3))
			return STRAIGHT_FLUSH;
		else if (isThreeOfAKind(card1, card2, card3))
			return THREE_OF_A_KIND;
		else if (isStraight(card1, card2, card3))
			return STRAIGHT;
		else if (isFlush(card1, card2, card3))
			return FLUSH;
		else if (isPair(card1, card2, card3))
			return PAIR;
		else
			return HIGH_CARD;
	}

	public static String getCardFromHand(String hand, int index) {
		/*
		 * Method used to break inputed hand into individual cards. Returns one card at
		 * a time.
		 */
		String card1 = hand.substring(0, hand.indexOf(" "));
		String smallerHand = hand.substring(card1.length() + 1);
		String card2 = smallerHand.substring(0, smallerHand.indexOf(" "));
		String card3 = smallerHand.substring(smallerHand.indexOf(" ") + 1, smallerHand.length() - 1);

		if (index == 1)
			return card1;
		else if (index == 2)
			return card2;
		else
			return card3;
	}

	public static boolean isPair(String card1, String card2, String card3) {
		/*
		 * Uses individual cards to check if the hand contains a pair.
		 */
		return (card1.substring(0, 1).equals(card2.substring(0, 1))
				|| card2.substring(0, 1).equals(card3.substring(0, 1))
				|| card1.substring(0, 1).equals(card3.substring(0, 1)));
	}

	public static boolean isFlush(String card1, String card2, String card3) {
		/*
		 * Uses individual cards to check if hand contains a flush
		 */
		return (card1.substring(card1.length() - 1).equals(card2.substring(card2.length() - 1))
				&& card2.substring(card2.length() - 1).equals(card3.substring(card3.length() - 1)));
	}

	public static boolean isStraight(String card1, String card2, String card3) {
		/*
		 * Uses individual cards to check if hand contains a flush
		 */
		int card1Value = getCardValue(card1);
		int card2Value = getCardValue(card2);
		int card3Value = getCardValue(card3);

		int maxCard = Math.max(card1Value, Math.max(card2Value, card3Value));
		int minCard = -Math.max(-card1Value, Math.max(-card2Value, -card3Value));
		int midCard = (card1Value + card2Value + card3Value) - (maxCard + minCard);

		return (minCard + 1 == midCard && midCard + 1 == maxCard);
	}

	public static boolean isThreeOfAKind(String card1, String card2, String card3) {
		/*
		 * Uses individual cards to check if the hand contains a 3 of a kind
		 */
		return (card1.substring(0, 1).equals(card2.substring(0, 1))
				&& card2.substring(0, 1).equals(card3.substring(0, 1)));
	}

	public static boolean isStraightFlush(String card1, String card2, String card3) { // Not finished
		/*
		 * uses individual cards to see if the hand contains a straight flush
		 */

		int card1Value = getCardValue(card1);
		int card2Value = getCardValue(card2);
		int card3Value = getCardValue(card3);

		/*
		 * Gets the value of each card and puts them in order from least to greatest.
		 * Then checks if they contain both a straight and a flush.
		 */

		int maxCard = Math.max(card1Value, Math.max(card2Value, card3Value));
		int minCard = -Math.max(-card1Value, Math.max(-card2Value, -card3Value));
		int midCard = (card1Value + card2Value + card3Value) - (maxCard + minCard);

		if ((minCard + 1 == midCard && midCard + 1 == maxCard)
				&& (card1.substring(card1.length() - 1).equals(card2.substring(card2.length() - 1))
						&& card2.substring(card2.length() - 1).equals(card3.substring(card3.length() - 1))))
			return true;
		else
			return false;
	}

	public static int getValidAnte(Scanner in, int playerWallet) {
		/*
		 * Asks player to place an ante wager. Mandatory to play.
		 */
		System.out.print("Please place an ante wager ($" + MIN_BET + "-" + MAX_BET + "): ");

		boolean isValidInput = false;
		while (!isValidInput) {
			try {
				int temp = Integer.parseInt(in.nextLine());
				if (temp >= MIN_BET && temp <= MAX_BET && temp <= playerWallet)
					return temp;
				else
					System.out.print("Invalid bet, ($" + MIN_BET + "-" + MAX_BET + "): ");

			} catch (Exception ex) {
				System.out.print("Invalid bet, ($" + MIN_BET + "-" + MAX_BET + "): ");
			}
		}
		return 0;
	}

	public static int getValidPairPlus(Scanner in, int playerWallet) {
		/*
		 * If player said they want to place a pair plus wager, this method makes sure
		 * they place a valid one. Checks with wallet for validity.
		 */
		System.out.print("Please place pair plus wager ($" + MIN_BET + "-" + MAX_BET + "): ");

		boolean isValidInput = false;
		while (!isValidInput) {
			try {
				int temp = Integer.parseInt(in.nextLine());
				if (temp >= MIN_BET && temp <= MAX_BET && temp <= playerWallet)
					return temp;
				else
					System.out.print("Invalid bet, ($" + MIN_BET + "-" + MAX_BET + "): ");

			} catch (Exception ex) {
				System.out.print("Invalid bet, ($" + MIN_BET + "-" + MAX_BET + "): ");
			}
		}
		return 0;
	}

	public static int getCardValue(String card) {
		/*
		 * Used to determine the value of individual cards.
		 */
		if (card.length() == 3)
			return 10;

		String face = card.substring(0, 1);
		try {// Ask me earlobe?
			return Integer.parseInt(face);
		} catch (Exception ex) {
			if (face.equals("A"))
				return aceValue;
			else if (face.equals("K"))
				return kingValue;
			else if (face.equals("Q"))
				return queenValue;
			else
				return jackValue;
		}

	}

	public static String getCard() {
		return getFace() + getSuit();
	}

	public static String getFace() {
		int temp = (int) (Math.random() * NUM_FACES) + 2;

		if (temp <= 10)
			return "" + temp;
		if (temp == jackValue)
			return "J";
		else if (temp == queenValue)
			return "Q";
		else if (temp == kingValue)
			return "K";
		else
			return "A";

	}

	public static String getSuit() {
		int temp = (int) (Math.random() * NUM_SUITS);
		if (temp == 0)
			return "H";
		else if (temp == 1)
			return "D";
		else if (temp == 2)
			return "S";
		else
			return "C";
	}

	public static boolean isUnique(String playerHand, String card) {
		/*
		 * Checks the new card with the playerhand to see if it has already been drawn.
		 */
		return playerHand.indexOf(card) == -1;
	}

	public static boolean isUnique(String playerHand, String dealerHand, String card) {
		/*
		 * Checks with the playerhand and dealerhand to see if the new card is unique.
		 */
		return (playerHand.indexOf(card) == -1 && dealerHand.indexOf(card) == -1);
	}
}
