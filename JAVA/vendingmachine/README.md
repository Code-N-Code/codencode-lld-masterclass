# Vending Machine - Low Level Design (Java)

This module demonstrates the **Low Level Design (LLD) of a Vending
Machine** implemented in **Java**.\
The goal of this implementation is to help students understand how to
translate real-world systems into clean, maintainable object‑oriented
code.

This project is part of the **Code & Code LLD Masterclass repository**.

## 🎥 Video Explanation

LLD Playlist:
https://www.youtube.com/playlist?list=PL5DyztRVgtRXc38XDgmL34o1pp7U\_\_hDK

Vending Machine Lecture: https://youtu.be/SxWF_RKQxEc

------------------------------------------------------------------------

# Problem Statement

Design a vending machine that:

1.  Allows a user to select a product
2.  Accepts coins
3.  Dispenses the selected product
4.  Returns change when extra money is inserted
5.  Handles insufficient payment
6.  Handles out-of-stock items

------------------------------------------------------------------------

# Core Components

## 1. VendingMachine

This is the **main class** responsible for controlling the vending
machine behavior.

Responsibilities:

-   Allow item selection
-   Accept coins
-   Track inserted money
-   Dispense product
-   Return change
-   Manage inventory

Typical functions:

    selectItem()
    insertCoin()
    collectItemAndChange()
    refund()
    reset()

------------------------------------------------------------------------

## 2. Item / Product

Represents products stored in the vending machine.

Attributes:

    name
    price

Example products:

    Coke
    Pepsi
    Soda

------------------------------------------------------------------------

## 3. Coin

An **enum** representing supported coin denominations.

Example:

    ONE
    FIVE
    TEN
    TWENTY

Each enum constant contains its value.

Example:

    ONE(1)
    FIVE(5)
    TEN(10)

------------------------------------------------------------------------

## 4. Inventory

A **generic inventory class** used for managing quantities.

It is used for:

-   Item inventory
-   Coin inventory

Responsibilities:

    addItem()
    removeItem()
    getQuantity()
    hasItem()
    clear()

Using generics allows the same inventory logic to be reused for multiple
object types.

------------------------------------------------------------------------

## 5. Bucket

A simple utility class used to return:

    Item
    +
    Change

after a successful purchase.

Example:

    Bucket<Item, List<Coin>>

------------------------------------------------------------------------

## 6. Exceptions

Custom exceptions help model real-world error cases.

Examples:

    ItemNotSelectedException
    NotFullPaidException
    NotSufficientChangeException
    SoldOutException

Using custom exceptions makes the code easier to understand and
maintain.

------------------------------------------------------------------------

# Transaction Flow

Typical purchase flow:

1.  User selects item
2.  Machine displays item price
3.  User inserts coins
4.  Machine validates payment
5.  If payment sufficient:
    -   Item dispensed
    -   Change returned
6.  Inventory updated

Flow summary:

    Select Item
         ↓
    Insert Coins
         ↓
    Validate Payment
         ↓
    Dispense Item
         ↓
    Return Change

------------------------------------------------------------------------

# UML Class Diagram

Below is a conceptual UML representation of the system.

``` mermaid
classDiagram

class VendingMachine {
    +selectItem()
    +insertCoin()
    +collectItemAndChange()
    +refund()
}

class Item {
    name
    price
}

class Coin {
    ONE
    FIVE
    TEN
    TWENTY
}

class Inventory {
    addItem()
    removeItem()
    getQuantity()
}

class Bucket {
    item
    change
}

VendingMachine --> Inventory
VendingMachine --> Item
VendingMachine --> Coin
VendingMachine --> Bucket
```

GitHub automatically renders **Mermaid diagrams**, so the UML will
display visually in the README.

------------------------------------------------------------------------

# Design Principles Used

## Encapsulation

Each class manages its own responsibility.

Example:

-   Inventory handles quantity logic
-   VendingMachine handles transaction logic

------------------------------------------------------------------------

## Separation of Concerns

Responsibilities are split across classes instead of putting everything
in one class.

------------------------------------------------------------------------

## Reusability

The generic **Inventory class** can manage different object types.

------------------------------------------------------------------------

## Maintainability

The design makes it easy to:

-   Add new products
-   Add new coins
-   Extend functionality

------------------------------------------------------------------------

# Possible Enhancements

Students can extend this design by adding:

-   Card payment support
-   UPI / digital payments
-   Admin mode for refilling inventory
-   Machine state pattern
-   Logging system
-   Concurrent user handling

------------------------------------------------------------------------

# Learning Outcome

After studying this design students will understand:

-   How to approach **LLD interview problems**
-   How to convert a **real world system into Java classes**
-   How to design **clean and modular code**
-   How different classes collaborate in a system

------------------------------------------------------------------------

# Author

**Code & Code**

YouTube Channel\
https://www.youtube.com/@codencode

If you found this helpful, check out the full **LLD playlist** and
explore more system design examples.
