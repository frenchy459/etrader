PROGRAM INSTALLATION INSTRUCTIONS
=================================

    This text file is designed for a terminal width of at least 110 characters and a monowidth font.
    For best viewing experience, please disable word wrapping in your text editor. We are also assuming
    you are using IntelliJ.

1. Exclude extraneous "src" folders after pulling the repos
-----------------------------------------------------------

    If you pulled the entire "group_0105" folder:

        - expand "group_0105" folder
        - right-click "phase1" folder > choose "mark directory as excluded"
        - expand "phase2" folder
        - right-click "TraderPrototype" folder > choose "mark directory as excluded"

    If you pulled just the "phase2" folder:

        - expand "phase2" folder
        - right-click "TraderPrototype" folder > choose "mark directory as excluded"

2. Installing dependencies (JARs)
---------------------------------

    Our program requires some JAR files to run. You can download them as an archive from the following URL:

    http://individual.utoronto.ca/calculemus/csc207/TradePalsJARs.zip

    To install them:

        - download the archive to your machine
        - extract its contents to your "phase2" folder so that the JAR folders are located in "phase2"
        - you should end up with "phase2/guiJARs", "phase2/jsonJARs", "phase2/localizationJARs"
        - the new folders should show up in the IntelliJ project after extraction
        - in IntelliJ, right-click each JAR folder, choose "Add as Library", and click "Okay"
        - the default values for the "guiJARs" folder are: "guiJARs", "Project Library", "phase2" if the
          "group_0105" folder was pulled; they may differ slightly if the "phase2" folder was pulled

    Alternate installation instructions in case the above don't work for some reason:

        - File > Project Structure > Modules > Dependencies tab (under Name, next to Path) > phase2
        - Click the '+' on the right hand side next to Scope
        - Click "1 JARs or directories..."
        - select all the jars in zip file (see below for list)
        - click Apply and Okay

    Dependencies list as reference (zip contents):

        - appengine-api-1.0-sdk-1.4.2.jar
        - commons-lang3-3.11.jar
        - fontawesomefx-8.9.jar
        - google-maps-services-0.9.3.jar
        - gson-2.8.6.jar
        - jfoenix-8.0.10.jar
        - joda-time-2.10.6.jar
        - jxmaps-1.3.2.jar
        - kotlin-stdlib-1.4.0-rc.jar
        - okhttp-4.8.0.jar
        - okio-2.7.0.jar
        - slf4j-api-1.7.28.jar
        - slf4j-simple-1.7.28.jar

3. Run the program
------------------

        - expand the "phase2/src/Layer3" package.
        - right-click Main.java and pick "Run 'Main.main()'".
        - the program should now be running, and you should have arrived at our landing menu
        - you can now log in with an existing user, or create your own user to test the program

PROGRAM USAGE INSTRUCTIONS
==========================

    To make the program less tedious to test, we've set up a few default accounts:

    Username:       |  Password:        |  Type:
    ----------------|-------------------|------------------
    al              |  12345            |  basic user
    bob             |  12345            |  basic user
    cora            |  12345            |  basic user
    dana            |  12345            |  basic user
    earl            |  12345            |  basic user
    admin           |  12345            |  administrator

    Each of these user accounts already has their own item inventory, a wishlist, and a lendlist, and we've also
    set up randomly generated transactions between them, which are in various states, ranging from PENDING
    APPROVAL to COMPLETE. This should help with experiencing all of the program's various features.

    Note that the 'admin' user is an administrator account with very different functionality than the rest of the
    normal users. There is also the option to log in as a demo user and test out part of the system that way.

    The admin account acts as the "main admin" account, so it cannot be deleted.

    When registering a new basic account, an address is required. The program only accepts Ontario addresses at
    the moment (but this is extensible), and they must be entered in a certain format. Auto-completion should
    help with any formatting issues.

    The program should be straightforward to use without any further instructions.

    (But if there's time, we will include a more detailed walkthrough, or even a video.)

PHASE 2 FEATURE LIST
====================

1. Mandatory Extensions
-----------------------

    1.1. The admin user can undo certain basic user actions. Some examples are below.

         To undo the last deleted item:
            - administrate basic users accounts
            - pick a user
            - click "select user"
            - click "undelete item"
         To delete a recently added item from a user's wishlist/inventory:
            - administrate basic user accounts
            - pick a user
            - click "wishlist"/"inventory"
            - select item
            - click "delete"

    1.2. The program automatically suggests items to lend to a given user if it's on one person's wish list
         and on another's lending list.
         
         To test this out:
            - log in as a normal user
            - initiate a trade from their own wishlist
                - in the subsequent edit trade menu, the user can now also lend out an item that is in the
                  partner's wishlist, resulting in a reciprocal two-way trade
            - initiate a trade from their own lendlist
                - in the subsequent edit trade menu, the user can now also borrow an item that is in the
                  partner's lendlist, resulting in a reciprocal two-way trade

    1.3. The program has a new account type allowing a test-run without trading or interacting with users or
         admins. On the log-in screen, there is an option to select "Demo User". This account is for when
         someone wants to look at the functionality of the program without making any persistent changes.
         The demo1 account has two placeholder items in their inventory and one completed transaction
         pre-loaded in their transaction history. They can make item requests and initiate trades but both
         of these do not interact with the program and are voided upon logging out of the demo account.

    1.4. The admin can change all threshold values from inside the program.

    1.5. There is another status for accounts besides frozen / unfrozen.

2. Optional Extensions
----------------------

    2.1. Allowing the user to enter their home city so that only users from the same city are shown.

         In fact, the program allows a user to enter their full address.

    2.2. The administrators have more functionality than phase 1 and Undo abilities.

         TODO: List additional functionality.

    2.3. Users have additional account states and standings.

         Account States:
         
         - Away         An Away status is toggled by a user when they know they will not be active in the
                        system for a definite period of time. Attempting to initiate a trade with an account
                        that is marked as away will alert the current user. This status can be applied or
                        removed by basic users on their own account at any time.
         
         - Inactive     The Inactive status represents a user that has not been active in the system for an
                        extended period of time. When triggered, pending transactions are automatically
                        cancelled and all user's items are removed from the Marketplace. Admins can activate
                        this status for user accounts at any time. Admins can also see the last time a user
                        has logged into the system which helps with this decision.
         
         Account Standings:
         
         - Default      A neutral, default standing.
         
         - Frozen       From the phase 1 requirements. A negative standing where a user is unable to
                        initiate new trades, handle trade requests or request new items. A user with
                        this standing can alert an admin if they wish to be unfrozen.
         
         - Limited      A negative standing where a user has stricter trading thresholds. It is applied to
                        accounts that are unfrozen and can be removed manually by an admin, assuming
                        the user hasn't continued to misbehave.
         
         - Trusted      A positive standing where a user has more lenient thresholds. It is applied to accounts
                        that have completed 10 consecutive trades without being frozen.

    2.4. The text UI was replaced with a GUI.

3. Original Extensions
----------------------

    3.1.  Modern GUI design. We went beyond just the required Swing GUI, and put a lot of effort into making
          it pretty, useable, and extensible, so we thought we should list it here as well.

    3.2.  Address auto-completion using Google Maps API.

    3.3.  Advanced sorting can now be used to sort/filter the marketplace based on:

            - alphabetical order of item name
            - the date an item was added to marketplace
            - the items most wishlisted by other users
            - the items within the city of the current user
            - the distance from the current user's address (using Google Maps API)
            - the driving distance from the current user's address (using Google Maps API)

    3.4.  All user passwords are now RSA-encrypted for additional security.

    3.5.  Users can rate each transaction on a scale from 0 to 5 in half point increments, and view other
          traders' ratings in the detail view for an item from the marketplace.

    3.6.  Users can view the account states of other users to see whether they're Inactive or Away, in order
          to plan their trades accordingly.

    3.7.  Users can edit the items in an incoming trade request, as well as the type of trade (temporary /
          permanent) in addition to just the time and place.

    3.8.  Users can upload a picture for their items, because a picture is worth a thousand words.

    3.9.  All users have a user profile where they can change their address in case they move, set their status
          as Away, as well as upload a new profile picture.

    3.10. Admins have the option to initialize the repos with dummy data. This is very useful for testing
          purposes. This feature generates the 5 users mentioned earlier, 10 items for each of them, as well
          as numerous random transactions between them in various states, ranging from PENDING APPROVAL to
          COMPLETE. During program implementation, this feature also doubled as a property-based test of
          sorts. To test this functionality, first delete the following files from phase2/repoFiles:
          
          basicUsers.bin
          items.bin
          transactions.bin
          
          Then select the "initialize repos" option in the admin menu. NOTE: Other files should not be deleted
          (though they could always be restored from Markus).
          
          This feature could easily be extended to allow more customizability and random properties.

    3.11. All 'hard-coded' strings (including prompts and error messages) are loaded in using a easy-to-read,
          easy-to-edit configuration file (stored in phase2/config.json). This allows less tech-savvy users to
          edit the prompts, and could even allow for changing the output language of the program.

    NOTE: Due to time constraints and our group's overly optimistic/ambitious timelines, we didn't manage to
    implement some of these features fully, but our program's extensibility should make it trivial to complete
    them in the future.

DESIGN PATTERNS
===============

    1. Dependency Injection

       We used dependency injection extensively throughout our program.
    
       The EntityRepo interface in Layer2 is used so that the use cases are able to access the repos, which
       are in Layer3, and contain the entities.
       
       Due to the nature of serialization, we ran into the problem of entities being re-instantiated with new
       memory addresses every time an instance of a repo was created. To solve this, we instantiate one instance
       of each repo at the very beginning of program in the Trader class in Layer3/Controllers. These instances
       are then injected throughout the program to the other controllers, presenters and use cases. Upon
       shutdown, the repos write the entities to the external bin files.
       
       Since the GeoApiContext is designed to be a singleton in an application, we instantiate one instance
       of the GeoApiContext object with our project's unique API key at the very beginning of program in the
       Trader class in Layer3.Controllers. This instance is then injected throughout the program to the other
       controller, presenters and use cases. Upon shutdown, the GeoApiContext object will then call its
       shutdown method, otherwise the thread will remain instantiated in memory.

    2. Strategy Pattern

       Each of the sorting classes in Layer2.Sort extend an abstract class called Sort.

    3. Factory Method
    
       For all the ListView JavaFX objects utilized within a multitude of controllers in Layer 3, we needed
       to apply a Factory design pattern in order to facilitate the process of implementing unique List Cells
       within these ListViews.
       
       A ListView will by default create cells based on a selection of limited pre-built options already defined
       within the JavaFX framework, but these options have poor extensibility and as such required us to utilize
       custom List Cells.

       In order to implement these custom List Cells, we utilized JavaFX's ControllerFactory setter that allows
       one to define a new "template" for all ListCells within a particular ListView.
       
       Each Factory was built around a "ListCell type" - a List Cell that took in a specific entity
       (ListCell<Transaction>, ListCell<Item> etc.).
       
       We could then define multiple Cell templates/menus within those Factories for their respective ListCell
       Type, as seen with the ItemCellFactory (which contains menus for Inventory ItemCells & Marketplace
       ItemCells).
       
       Abstraction was utilized for Factories, ListCells and ListCellMenus to group up common functionality
       across all three of these class types.
       
       All List Cell related classes are stored within the Layer3.Controllers.Cells package, apart from the
       ListCell menus which are functionally identical to other controller menu classes (these are instead
       stored in Layer3.Controllers).

    4. Observer Pattern

       Layer3\Repos\AdminNotificationRepo implements the ObserverEntityRepo interface, which extends the
       Observer class.

       BasicUserManager, AdminNotificationManager, and NewItemManager all extend Observable.

       BasicUserManager - method compareIncompleteTransacWithThreshold calls setChanged()
       AdminNotificationManager - method createNewAdminNotification calls setChanged()
       NewItemManager - method createItem calls setChanged()

       We also made heavy use of JavaFX classes that implemented the Observer pattern.
       

POSSIBLE EXTENSIONS
===================

    We designed our program with extensibility in mind. Some other extensions we would have loved to implement
    if there had been time:

        Multiple item trades! Our entities already allow for this, and many of our use case methods already
        support this functionality. The main implementation challenge is in the Presenters/Controller layer.

        More design patterns! There are a lot of opportunities for using more design patterns, but we learned
        about them pretty late in the course, and time constraints prevented us from implementing more of them.
        In multiple places we end up passing a large number of parameters around the program via constructors.
        We could reduce code clutter by implementing a Facade, or a Builder, to encapsulate all the repos, for
        instance, and pass them in one class instead.

        Allow users to switch their interface! JavaFX makes it very easy to switch back and forth between
        different interface. Alister, our TA, will remember that we showed him a completely different interface
        during our initial walkthrough. Yet, it's the same program! And we could easily extend our program
        to allow a user to pick and choose among a number of different interfaces and themes.

KNOWN ISSUES
============

    Due to the tight time constraints and us being overly ambitious and optimistic about what we could get done
    in the given time, we left a lot of things left undone. None of it would be particularly challenging to
    implement, we just ran out of time. Sadly, we'll only get a chance to fix these things once the course is
    over.

    UX Issues:

        A lot of menus display unnecessary horizontal scroll bars.

        Some menus don't display anything when they're devoid of data, and the user is not given any feedback
        that there is no data to populate them with, and could easily think the program crashed or there
        was something wrong.

        Some text fields in the interface fail to wrap around to the next line and get cut off (for example
        addresses, time strings, item descriptions, etc.)

        Some menu windows are much larger than they need to be.

	Coding style:

		Some classes are too large, some are too small. Unfortunately we had very little time to refactor,
		since we had our hands full with implementing features and functionality.

    Bugs:

        Demo user has some backend exception issues in certain menus, though it doesn't affect program
        functionality.

        Auto-complete can react slowly at times, and doesn't kick in after every letter.

        If a user requests an item approval, and the admin deletes the system notification informing them of
        the request, the item request is deleted as well.
