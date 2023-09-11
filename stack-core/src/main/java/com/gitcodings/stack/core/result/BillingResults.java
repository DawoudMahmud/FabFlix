package com.gitcodings.stack.core.result;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public final class BillingResults
{
    private BillingResults() { throw new AssertionError("No Instances of Results can be created"); }

    public static final Result INVALID_QUANTITY =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("Quantity cannot be zero or less")
            .code(3000)
            .build();

    public static final Result MAX_QUANTITY =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("Quantity exceeded max limit")
            .code(3001)
            .build();

    public static final Result CART_ITEM_EXISTS =
        new Result.Builder()
            .status(HttpStatus.CONFLICT)
            .message("Item already in cart")
            .code(3002)
            .build();

    public static final Result CART_ITEM_DOES_NOT_EXIST =
        new Result.Builder()
            .status(HttpStatus.CONFLICT)
            .message("Item not in cart")
            .code(3003)
            .build();

    public static final Result CART_EMPTY =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Cart is empty")
            .code(3004)
            .build();

    public static final Result STRIPE_ERROR =
        new Result.Builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .message("Stripe encountered and error")
            .code(3005)
            .build();

    public static final Result CART_ITEM_INSERTED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Item inserted into cart")
            .code(3010)
            .build();

    public static final Result CART_ITEM_UPDATED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Item in cart updated")
            .code(3020)
            .build();

    public static final Result CART_ITEM_DELETED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Item deleted from cart")
            .code(3030)
            .build();

    public static final Result CART_RETRIEVED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Cart items retrieved")
            .code(3040)
            .build();

    public static final Result CART_CLEARED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Items have been cleared from cart")
            .code(3050)
            .build();

    public static final Result ORDER_PAYMENT_INTENT_CREATED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Order Payment Intent Created")
            .code(3060)
            .build();

    public static final Result ORDER_COMPLETED =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Order Completed")
            .code(3070)
            .build();

    public static final Result ORDER_CANNOT_COMPLETE_NOT_SUCCEEDED =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("The order has not yet been paid")
            .code(3071)
            .build();

    public static final Result ORDER_CANNOT_COMPLETE_WRONG_USER =
        new Result.Builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("The order is not meant for this user")
            .code(3072)
            .build();

    public static final Result ORDER_LIST_FOUND_SALES =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Sales found for user")
            .code(3080)
            .build();

    public static final Result ORDER_LIST_NO_SALES_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("No sales found for user")
            .code(3081)
            .build();

    public static final Result ORDER_DETAIL_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("Sale details found for saleId")
            .code(3090)
            .build();

    public static final Result ORDER_DETAIL_NOT_FOUND =
        new Result.Builder()
            .status(HttpStatus.OK)
            .message("No sale details found for saleId")
            .code(3091)
            .build();

    static Stream<Result> toStream()
    {
        return Stream.of(
            INVALID_QUANTITY,
            MAX_QUANTITY,
            CART_ITEM_EXISTS,
            CART_ITEM_DOES_NOT_EXIST,
            CART_EMPTY,
            STRIPE_ERROR,
            CART_ITEM_INSERTED,
            CART_ITEM_UPDATED,
            CART_ITEM_DELETED,
            CART_RETRIEVED,
            CART_CLEARED,
            ORDER_PAYMENT_INTENT_CREATED,
            ORDER_COMPLETED,
            ORDER_CANNOT_COMPLETE_NOT_SUCCEEDED,
            ORDER_CANNOT_COMPLETE_WRONG_USER,
            ORDER_LIST_FOUND_SALES,
            ORDER_LIST_NO_SALES_FOUND,
            ORDER_DETAIL_FOUND,
            ORDER_DETAIL_NOT_FOUND
        );
    }
}
