package view;

import dao.*;
import util.DBManager;
import util.IOManager;

public class Purchase_View {

    public static Card cache_card;
    public static void getView()
    {
        Vendor vendor;

        if (IOManager.yesNo("Welcome to the purchase interface, would you like to buy something? (Y)es, (N)o"))
        {
            Purchase_View.getPurchaseView();
        }
    }

    public static void getPurchaseView()
    {
        Card card = Purchase_View.getCard();

        if (card == null)
        {
            System.out.println("Cancelling purchase process.");
            return;
        }

        System.out.println(card);

        Vendor vendor = Purchase_View.getVendor();

        boolean repeat = true;
        while (repeat) {
            System.out.println("How much would you like to pay " + vendor.getName() + "?");
            double amount = IOManager.getInputDouble(0);

            if (card.getAvailableBalance() < amount) {
                System.out.println("This payment will overdraw your card and thus cannot be allowed");
                if (IOManager.yesNo("Would you like to pay a different amount? (Y)es, (N)o")) {
                    repeat = true;
                } else {
                    repeat = false;
                }
                continue;
            } else if (card instanceof Card_Debit && ((Card_Debit) card).getAccount().getBalance() - amount < ((Card_Debit) card).getAccount().getMinBalance()) {

                System.out.println("This payment will take your account below the minimum balance and may subject you to fees.");
                if (!IOManager.yesNo("Are you sure you wish to continue? (Y)es, (N)o")) {
                    System.out.println("Canceling withdrawal process.\n");
                    repeat = false;
                    continue;
                }
            }

            if (IOManager.yesNo("You are about to pay " + IOManager.formatCurrency(amount) + " to " + vendor.getName() + " is this correct? (Y)es, (N)o")) {
                repeat = false;
                card.pay(amount, vendor);
            } else if (!IOManager.yesNo("Would you like to pay a different amount? (Y)es, (N)o")) {
                System.out.println("Canceling withdrawal process.\n");
                repeat = false;
            }
        }

        Purchase_View.cacheCard(card);
    }

    public static Card getCard()
    {
        if (Purchase_View.cache_card != null && IOManager.yesNo("We have the card " +
            Purchase_View.cache_card + " handy, is this the one you want? (Y)es, (N)o"))
        {
            return Purchase_View.cache_card;
        }

        System.out.println("Please input your card number.");
        long card_number = IOManager.getInputLong(1000000000000000l, 9999999999999999l);

        System.out.println("Please input your CVC, it's the little numbers on the back of your card!");
        int cvc = IOManager.getInputInt(100, 10000);

        Card card = Card.fromCardNumber(card_number, cvc);

        if (card == null)
        {
            if (IOManager.yesNo("The credentials " + card_number + " with cvc " + cvc + " do not match a valid card. Do you wish to try again? (Y)es, (N)o"))
            {
                return getCard();
            }

            return null;
        }

        if (!"active".equals(card.getType()))
        {
            if (IOManager.yesNo("The credentials " + card_number + " with cvc " + cvc + " represent a card which is not active. Do you wish to try again? (Y)es, (N)o"))
            {
                return getCard();
            }

            return null;
        }

        return card;
    }

    public static Vendor getVendor()
    {
        System.out.println("Who would you like to pay?");
        String name = IOManager.getInputString();

        Vendor vendor = Vendor.fromName(name);

        if (vendor == null)
        {
            vendor = new Vendor(name);
        }

        return vendor;
    }

    public static void cacheCard(Card card)
    {
        if (IOManager.yesNo("\nShould we keep this card handy for future transactions? (Y)es, (N)o")) {
            System.out.println("We'll keep it nearby then!");
            Purchase_View.cache_card = card;
        }
        else
        {
            Purchase_View.cache_card = null;
        }
    }
}
