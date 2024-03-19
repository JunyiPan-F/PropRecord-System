Junyi Pan

Usage:
- Enter 'jup324Pan' folder using `cd jup324Pan`
- Run the program using `java -jar jup324.jar`


1. Property Manager

        the property manager menu has operations belew
        ----------------------------------Property Manager Menu----------------------------------
        Select your action from below:
        1. record visit data
        2. record lease data
        3. record move-out
        4. add person or pet to a lease
        5. set move-out date
        Enter 0 to go back to main menu.

    1.1 record visit data
        - used to record the visit data
        - the following prompt is one example of how to add the data
        - the menu will print out property, and apartment information needed
        Example:
            Please enter the name of the prospecting tenant: 
            Florence P
            Please enter the phone number of the prospecting tenant: 
            5102040248
            Please enter the email of the prospecting tenant: 
            jp@jp.com
            This is the list of properties: 
            5x10
            ......
            Sayre House
            Please enter the property name that prospecting tenant visited (ex. Soutside): 
            Sayre House
            These are the apartments in this property: 
            C412
            ......
            B622
            Please enter the apartment number the prospecting tenant visited: 
            C412
            Please enter the visit time of the prospecting tenant: (MM-dd-yyyy)
            12-08-2023

    1.2 record lease data
        - This is used to record the lease data, lease id is automatically generated to ensure the uniqueness
        - the following prompt is one example of how to add the data
        - the menu will print out tenant, property, and apartment information needed
        Example:
                Please enter the tenant information:
                These are the existing tenants: 
                ID      First Name      Last Name
                T00000001       jed     ade
                ....
                T00000011       Kaushik Duvur

                Please enter the tenant ID (ex. T00000001): 
                T00000011
                Please enter the start day of the lease: (MM-dd-yyyy)
                01-01-2023
                Please enter the end day of the lease: (MM/DD/YYYY)
                12-01-2023
                Here are all the properties: 
                5x10
                Coffee House
                ....
                Sunny Lane
                Please enter the property name from list above: 
                Sunny Lane
                These are the apartments in this property: 
                A212
                C607
                Please enter the apartment number from above: 
                A212

    1.3 record move out (exception: info wrong, no lease found)
        - This is used to record move out
        - The notice in this option is that the user need to know the property name and apartment number associated with the tenant
        - This is because in the real life senario, it is more realistic for people to know the property name and apartment number instead of lease id
        - the following prompt is one example of how to record move out
        - the menu will print out tenant, property, and apartment information needed

        Example:
            Here are all the properties: 
            5x10
            ......
            Sunny Lane
            Please enter the property name: 
            Sunny Lane
            These are the apartments in this property: 
            A212
            C607
            Please enter the apartment number: 
            A212
            These are the existing tenants: 
            ID      First Name      Last Name
            T00000001       jed     ade
            ......
            T00000011       Kaushik Duvur
            Please enter the tenant ID: 
            T00000011

    1.4 add person or pet to a lease
        - This is used to 1. add a roommate 2. add a pet
        - the following prompt is one example
        - the menu will print out tenant, property, and apartment information needed
        Example (add rommate):
            Enter 1 to add a person OR 2 to add a pet.
            1
            These are the existing tenants: 
            ID              First Name      Last Name
            T00000010       Kevin           Dotel
            T00000011       Kaushik         Duvur
            ......
            Please enter the tenant ID: 
            T00000011
            Please enter the roommate's ID: 
            T00000010
            Roommate added.

        Example (add pet):
            Enter 1 to add a person OR 2 to add a pet.
            2
            These are the existing tenants: 
            ID              First Name      Last Name
            T00000018       Joyce           Guo
            ......
            Please enter the tenant ID: 
            T00000018
            Pet added.

    1.5 set move-out date (TODO)
        - This is used to set move out date
        - The notice in this option is that the user need to know the property name and apartment number associated with the tenant
        - This is because in the real life senario, it is more realistic for people to know the property name and apartment number instead of lease id
        - the following prompt is one example of how to set move out
        - the menu will print out tenant, property, and apartment information needed

        Example:

            Here are all the properties: 
            Southside
            ......
            Please enter the property name: 
            Southside
            These are the apartments in this property: 
            A101
            ......
            Please enter the apartment number: 
            A101
            These are the existing tenants: 
            ID              First Name      Last Name
            T00000003       Jouny           Zedan
            ......
            Please enter the tenant ID: 
            T00000003
            Previous move-out date: 2024-09-30
            Please enter the new move-out date: (MM-dd-yyyy)
            10-01-2024
            Move-out date updated.
            Refund updated.

2. Tenant

    - Before entering tenant menu, tenant id will be needed to proceed
    - All tenant ids are printed with information
    - You can choose T00000001

    ----------------------------------Tenant Menu----------------------------------
    Select your action from below:
    1. check payment status (amount due, if any)
    2. make rental payment
    3. update personal data
    Enter 0 to go back to main menu.


    2.1 check payment status (amount due, if any)
        - This is used to check for the amount due of the lease(s) associated with the tenant
        - the following prompt is one example of how to enter the data
        - the menu will print out lease information needed
        Example:
            These are the payments associated with the tenant: 
            ID              Property Name   Apartment Number
            L00000001       Southside       A101
            To check payment status, please enter the information below
            Please enter the property name: 
            Southside
            Please enter the apartment number: 
            A101
            Amount: 15724.0

    2.2 make rental payment
        - This is used to make rental payment according to the information given in 2.1 process
        - the following prompt is one example of how to enter the data
        - You can choose to make payment in 3 ways (Online payment, Cash, Card)
        - the menu will print out lease information needed

        Example (Online payment):

            Please enter the property name: 
            Southside
            Please enter the apartment number: 
            A101
            Total rent left: 15724.0
            Please enter the payment method (1-3): 
            1. Online payment
            2. Cash
            3. Card
            1
            Please enter the name of online payment party: 
            Paypal
            Please enter the associated email with your payment party: 
            jup324@lehigh.edu
            Please enter the amount you want to pay: 
            3000
            Third Party Payment went through.
        
        Simplied example (Cash):
            
            Please enter the name of currency: 
            USD
            Please enter the amount you want to pay: 
            10
            Cash Payment went through.

        Simplied example (Card):

        Validation:
            - Go back to 2.1 check payment status (amount due, if any)
            - If payment went through, the amount due should change accordingly
            - Following the instruction to check amount due gives results below:

                These are the payments associated with the tenant: 
                ID              Property Name   Apartment Number
                L00000001       Southside       A101
                To check payment status, please enter the information below
                Please enter the property name: 
                Southside
                Please enter the apartment number: 
                A101
                Amount: 12724.0

    2.3 update personal data
        - This is used to make changes to your personal data
        - It prints out your info
        - the following prompt is one example of how to enter the data
        - the menu will print out lease information needed

        Which information do you want to update: 
        1. Name
        2. Phone
        3. Email
        1
        Please enter your new name: 
        Junyi Pan
        new name: Junyi Pan
        enter 0 to confirm
        0
        Name updated.

3. company manager

    ----------------------------------Company Manager Menu----------------------------------
    Select your action from below:
    1. Add a new property
    (Provide location, amenities, apartments, etc.)
    2. Generate Data for each apartment
    (Provide size, bedrooms, bathroom, features, amenities, etc.)
    Enter 0 to go back to main menu.

    3.1. Add a new property (Provide location, amenities, apartments, etc.)
        - This is used to add a new property to the entreprise
        - the following prompt is one example of how to enter the data
        - It prints out the common amenities that you can choose to add to this property

        Example:

            Please enter the new property name: 
            New House
            Please enter the new property address: 
            420 FIllless sT
            Here are all existng Common amenities: 
            Bar
            ......
            Please enter the common amenities you want to add to this property from the above list, separated by comma: Example: Swimming Pool,Gym,Spa
            Study Room,Spa,Bar

    3.2. Generate Data for each apartment (Provide size, bedrooms, bathroom, features, amenities, etc.)
        - This is used to add a new apartment to the property
        - the following prompt is one example of how to enter the data

        Example:

            Here are all the property listed below:
            New House
            ......
            Please enter the property name you want to add apartment to: 
            New House
            Please enter the apartment number that you want to add (ex.C101): 
            B222
            Please enter the apartment information as below: 
            room_size(m^2), bedroom_no, bathroom_no, rent
            Example: 800,2,1,1000
            800,2,1,1000
            Apartment added.

4. Financial Manager

    ----------------------------------Financial Manager Menu----------------------------------
    Select your action from below:
    1. collect aggregate data about a property
    2. collect aggregate data about some set of properties
    3. collect aggregate data about the entire enterprise
    4. produces financial reports.
    Enter 0 to go back to main menu.

    4.1. collect aggregate data about a property
        - This is used to generate data about a property (name, location, common amenities, etc)
        - the following prompt is one example of how to enter the data
        - You will be asked to choose if you want to generate revenue financial report on this property
        - the menu will print out property information needed

        Example:
            Here are all the property listed below:
            ......
            11. Southside
            ......
            Please enter the property name: 
            Southside
            Property name: Southside
            Property address: 444 Brodhead Avenue, Bethlehem, PA 18015
            Common amenities: 
            Gym
            Dog Park
            Spa
            Study Room
            Parking Lot
            Swimming Pool
            If you want to see the financial report about this property, please enter 1, otherwise enter 0: 
            1
            Total revenue: 2320

    4.2. collect aggregate data about some set of properties
        - This is used to generate data about properties that you enter (name, location, common amenities, total revenue)
        - the following prompt is one example of how to enter the data
        - the menu will print out property information needed

        Example:

            Please enter the property names, separated by comma: 
            Example:Southside,5x10,Sayre House,HST
            Southside,5x10,Sayre House,HST

            Property name: Southside
            Property address: 444 Brodhead Avenue, Bethlehem, PA 18015
            Common amenities: 
            Gym
            Dog Park
            Spa
            Study Room
            Parking Lot
            Swimming Pool
            Total revenue: 4440

            Property name: 5x10
            Property address: 456 Elm St, Apt 202, Los Angeles, California, 90001
            No common amenity in this property.

            Property name: Sayre House
            Property address: 4040 Willow St, Apt 808, Seattle, Washington, 98101
            Common amenities: 
            Swimming Pool
            No lease in this property.
            Total revenue: 0

            Property name: HST
            Property address: 5050 Redwood St, Apt 909, Boston, Massachusetts, 02101
            Common amenities: 
            Parking Lot
            Total revenue: 0

    4.3. collect aggregate data about the entire enterprise
        - This is used to generate data about all the properties(name, location, common amenities, total revenue)
        - This doesn't require any input

    4.4. produces financial reports.
        - This is used to generate the total revenue of the whole enterprise
        - This doesn't require any input