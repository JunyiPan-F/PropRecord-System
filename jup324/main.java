import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
public class main {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
    
    /*
     * This method is used to select the interface.
     * @return the interface number
     */
    public static int select_interface(Scanner input) {
        int choice = 0;
        while (true) 
        {
            System.out.println();
            System.out.println("--------------------------------Welcome to the Rental Management System--------------------------------");
            System.out.println("Select your section from below:");
            System.out.println("1. Property manager");
            System.out.println("2. Tenant");
            System.out.println("3. Company manager");
            System.out.println("4. Financial manager");
            System.out.println("Enter 0 to exit.");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 0 && choice <= 4) 
                {
                    break;
                }
                System.out.println("Invalid operation. Must be an integer from 0 to 4.");
            }
            else 
            {
                input.nextLine();
                System.out.println("Invalid operation. Must be an integer.");
            }
        }
        return choice;
    }

    /*
     * This method is used to display the property manager menu.
     */
    public static int proterty_manager_menu(Scanner input) {
        int choice = 0;
        while (true) 
        {
            System.out.println();
            System.out.println("----------------------------------Property Manager Menu----------------------------------");
            System.out.println("Select your action from below:");
            System.out.println("1. record visit data");
            System.out.println("2. record lease data");
            System.out.println("3. record move-out");
            System.out.println("4. add person or pet to a lease");
            System.out.println("5. set move-out date");
            System.out.println("Enter 0 to go back to main menu.");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 0 && choice <= 5) 
                {
                    break;
                }
                System.out.println("Invalid operation. Must be an integer from 0 to 5.");
            }
            else 
            {
                input.nextLine();
                System.out.println("Invalid operation. Must be an integer.");
            }
        }
        return choice;
    }

    /*
     * This method is used to perform the property manager action.
     */
    public static void pm_action(int action, Connection conn, Scanner input) {
        try{
            // prepare statement
            PreparedStatement get_prop = conn.prepareStatement(
                "select name from property");
            ResultSet prop_list;
            PreparedStatement check_prop;
            ResultSet prop_res;
            PreparedStatement check_apart;
            ResultSet apart_res;
            String prop_name = "";
            String apart_no = "";
            String lease_id = "";
            String tenant_id = "";
            int tenant_num = 0;
            int lease_num = 0;
            double base_rent = 0;
            java.sql.Date prev_end_date = null;
            double amenity_cost = 0;
            switch (action) {
                case 0:
                    break;
                case 1:
                    // record visit data
                    // add data to Prospecting Tenants table

                    System.out.println("Please enter the name of the prospecting tenant: ");
                    input.nextLine();
                    String name = input.nextLine();
                    System.out.println("Please enter the phone number of the prospecting tenant: ");
                    String phone = input.next();
                    System.out.println("Please enter the email of the prospecting tenant: ");
                    String email = input.next();
                    
                    System.out.println("This is the list of properties: ");
                    //execute
                    prop_list = get_prop.executeQuery();
                    if(!prop_list.next()){
                        System.out.println("Property not found.");
                        break;
                    }
                    do{
                        System.out.println(prop_list.getString(1));
                    }while(prop_list.next());

                    System.out.println("Please enter the property name that prospecting tenant visited (ex. Soutside): ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    // check if prop_name exists
                    check_prop = conn.prepareStatement(
                        "select name from property where name=?");
                    check_prop.setString(1, prop_name);
                    prop_res = check_prop.executeQuery();
                    if (prop_res.next()) {
                        prop_name = prop_res.getString(1);
                    }else{
                        System.out.println("Property not found, enter correct property name.");
                        prop_name = input.next();
                        while(prop_name == ""){
                            System.out.println("Property not found, enter correct property name.");
                            prop_name = input.next();
                        }
                    }
                    
                    check_apart = conn.prepareStatement(
                        "select apart_no from apartment where name=?");
                    check_apart.setString(1, prop_name);
                    apart_res = check_apart.executeQuery();
                    if (!apart_res.next()) {
                        System.out.println("Apartment not found.");
                        break;
                    }
                    System.out.println("These are the apartments in this property: ");
                    do{
                        System.out.println(apart_res.getString(1));
                    }while(apart_res.next());

                    System.out.println("Please enter the apartment number the prospecting tenant visited: ");
                    apart_no = input.next();
                    // check if apart_no exists
                    check_apart = conn.prepareStatement(
                        "select apart_no from apartment where apart_no=?");
                    check_apart.setString(1, apart_no);
                    apart_res = check_apart.executeQuery();
                   
                    if (apart_res.next()) {
                        apart_no = apart_res.getString(1);
                    }else{
                        System.out.println("Apartment not found, enter correct apartment number.");
                        apart_no = input.next();
                        while(apart_no == ""){
                            System.out.println("Apartment not found, enter correct apartment number.");
                            apart_no = input.next();
                        }
                    }
                    System.out.println("Please enter the visit time of the prospecting tenant: (MM-dd-yyyy)");
                    String visit_time = input.next();
                    //convert string to date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date parsedDate = dateFormat.parse(visit_time);
                    java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                    // close scanner
                    // insert data into table
                    // prepare statement
                    PreparedStatement add_ProspTenant = conn.prepareStatement(
                        "insert into Prospecting_Tenants values(?, ?, ?, ?, ?, ?, ?)");
                    // set values
                    // generate tenant id
                    // get number of entries in tenant table
                    PreparedStatement get_tenant_num = conn.prepareStatement(
                        "select count(*) from prospecting_tenants");
                    ResultSet tenant_num_res = get_tenant_num.executeQuery();
                    if (tenant_num_res.next()) {
                        tenant_num = tenant_num_res.getInt(1);
                    }else{
                        System.out.println("Tenant not found.");
                    }
                    tenant_num++;
                    // count number of digits in tenant_num
                    if(tenant_num < 10){
                        tenant_id = "PT0000" + tenant_num;
                    }else if(tenant_num < 100){
                        tenant_id = "PT000" + tenant_num;
                    }else if(tenant_num < 1000){
                        tenant_id = "PT00" + tenant_num;
                    }else if(tenant_num < 10000){
                        tenant_id = "PT0" + tenant_num;
                    }else{
                        tenant_id = "PT" + tenant_num;
                    }
                    
                    add_ProspTenant.setString(1, tenant_id);
                    add_ProspTenant.setString(2, name);
                    add_ProspTenant.setString(3, phone);
                    add_ProspTenant.setString(4, email);
                    add_ProspTenant.setDate(5, sqlDate);
                    add_ProspTenant.setString(6, apart_no);
                    add_ProspTenant.setString(7, prop_name);
                    //execute
                    add_ProspTenant.executeUpdate();
                    System.out.println("Data successfully inserted.");
                    break;
                case 2:
                    // record lease data

                    // generate lease id
                    // get number of entries in lease table
                    PreparedStatement get_lease_num = conn.prepareStatement(
                        "select count(*) from lease");
                    ResultSet lease_num_res = get_lease_num.executeQuery();
                    if (lease_num_res.next()){
                        lease_num = lease_num_res.getInt(1);
                    }
                    lease_num++;
                    // count number of digits in lease_num
                    if(lease_num < 10){
                        lease_id = "L0000000" + lease_num;
                    }else if(lease_num < 100){
                        lease_id = "L000000" + lease_num;
                    }else if(lease_num < 1000){
                        lease_id = "L00000" + lease_num;
                    }else if(lease_num < 10000){
                        lease_id = "L0000" + lease_num;
                    }else if(lease_num < 100000){
                        lease_id = "L000" + lease_num;
                    }else if(lease_num < 1000000){
                        lease_id = "L00" + lease_num;
                    }else if(lease_num < 10000000){
                        lease_id = "L0" + lease_num;
                    }else{
                        lease_id = "L" + lease_num;
                    }
                    System.out.println("Please enter the tenant information:");
                    System.out.println("These are the existing tenants: ");
                    System.out.println("ID \tFirst Name \tLast Name");
                    // prepare statement
                    PreparedStatement get_tenant = conn.prepareStatement(
                        "select id, first_name, last_name from tenant");
                    //execute
                    ResultSet tenant_res = get_tenant.executeQuery();
                    if (!tenant_res.next()) {
                        System.out.println("Tenant not found.");
                        break;
                    }
                    do{
                        System.out.println(tenant_res.getString(1) + "\t" + tenant_res.getString(2) + "\t" + tenant_res.getString(3));
                    }while(tenant_res.next());
                    System.out.println("Please enter the tenant ID (ex. T00000001): ");
                    tenant_id = input.next();


                    System.out.println("Please enter the start day of the lease: (MM-dd-yyyy)");
                    String start_day = input.next();
                    //convert string to date
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date parsed_start_date = dateFormat.parse(start_day);
                    java.sql.Date sql_start_date = new java.sql.Date(parsed_start_date.getTime());

                    System.out.println("Please enter the end day of the lease: (MM/DD/YYYY)");
                    String end_day = input.next();
                    //convert string to date
                    Date parsed_end_date = dateFormat.parse(end_day);
                    java.sql.Date sql_end_date = new java.sql.Date(parsed_end_date.getTime());
                    // number of months
                    int num_month = (int) ((sql_end_date.getTime() - sql_start_date.getTime()) / (1000 * 60 * 60 * 24 * 30));
                    
                    boolean valid = false;
                    System.out.println("Here are all the properties: ");
                    //execute
                    prop_list = get_prop.executeQuery();
                    if(!prop_list.next()){
                        System.out.println("Property not found.");
                        break;
                    }
                    do{
                        System.out.println(prop_list.getString(1));
                    }while(prop_list.next());


                    while(!valid){
                        System.out.println("Please enter the property name from list above: ");
                        while (!input.hasNextLine()) {
                            System.out.println("Please enter the property name in correct format: ");
                        }
                        input.nextLine();
                        prop_name = input.nextLine();
                        //check if prop_name exists
                        check_prop = conn.prepareStatement(
                            "select name from property where name=?");
                        check_prop.setString(1, prop_name);
                        prop_res = check_prop.executeQuery();
                        if (prop_res.next()) {
                            prop_name = prop_res.getString(1);
                        }else{
                            System.out.println("Property not found, enter correct property name.");
                            prop_name = input.next();
                            while(prop_name == ""){
                                System.out.println("Property not found, enter correct property name.");
                                prop_name = input.next();
                            }
                        }
                        valid = true;
                    }

                    check_apart = conn.prepareStatement(
                        "select apart_no from apartment where name=?");
                    check_apart.setString(1, prop_name);
                    apart_res = check_apart.executeQuery();
                    if (!apart_res.next()) {
                        System.out.println("Apartment not found.");
                        break;
                    }
                    System.out.println("These are the apartments in this property: ");
                    do{
                        System.out.println(apart_res.getString(1));
                    }while(apart_res.next());

                    valid = false;
                    while(!valid){
                        System.out.println("Please enter the apartment number from above: ");
                        while (!input.hasNext()) {
                            System.out.println("Please enter the apartment number in correct format: ");
                        }
                        apart_no = input.next();
                        //check if apart_no exists
                        check_apart = conn.prepareStatement(
                            "select apart_no from apartment where apart_no=? and name=?");
                        check_apart.setString(1, apart_no);
                        check_apart.setString(2, prop_name);
                        apart_res = check_apart.executeQuery();
                        if (apart_res.next()) {
                            apart_no = apart_res.getString(1);
                        }else{
                            System.out.println("Apartment not found, enter correct apartment number.");
                            apart_no = input.next();
                            while(apart_no == ""){
                                System.out.println("Apartment not found, enter correct apartment number.");
                                apart_no = input.next();
                            }
                        }
                        valid = true;
                    }

                    // search the apartment for the rent
                    // prepare statement
                    PreparedStatement get_Rent = conn.prepareStatement(
                        "select rent from apartment where apart_no=? and name=?");
                    //set values
                    get_Rent.setString(1, apart_no);
                    get_Rent.setString(2, prop_name);
                    //execute
                    ResultSet rent_res = get_Rent.executeQuery();
                    if (rent_res.next()) {
                        base_rent = rent_res.getDouble(1);
                    }else{
                        System.out.println("Rent not found, enter correct apartment number and property name.");
                        apart_no = input.next();
                        while(apart_no == ""){
                            System.out.println("Rent not found, enter correct apartment number and property name.");
                            apart_no = input.next();
                        }
                    }

                    // calculate amenity cost
                    // prepare statement
                    PreparedStatement get_apt_amenitylist = conn.prepareStatement(
                        "select amenity_name from apt_has_amenity where prop_name=? and apart_no=?");
                    //set values
                    get_apt_amenitylist.setString(1, prop_name);
                    get_apt_amenitylist.setString(2, apart_no);
                    //execute
                    ResultSet apt_amenitylist_res = get_apt_amenitylist.executeQuery();
                    while(apt_amenitylist_res.next()){
                        // prepare statement
                        PreparedStatement get_amenity_cost = conn.prepareStatement(
                            "select cost from amenity where name=?");
                        //set values
                        get_amenity_cost.setString(1, apt_amenitylist_res.getString(1));
                        //execute
                        ResultSet amenity_cost_res = get_amenity_cost.executeQuery();
                        if(amenity_cost_res.next()){
                            amenity_cost += amenity_cost_res.getDouble(1);
                        }
                    }

                    PreparedStatement add_lease = conn.prepareStatement("insert into lease values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    add_lease.setString(1, lease_id);
                    add_lease.setDate(2, sql_start_date);
                    add_lease.setDate(3, sql_end_date);
                    add_lease.setInt(4, num_month);
                    add_lease.setDouble(5, 0);// refound is 0 for now when lease is recorded
                    add_lease.setDouble(6, amenity_cost);
                    add_lease.setDouble(7, base_rent);
                    add_lease.setDouble(8, base_rent);// deposit is the same as base rent
                    add_lease.setString(9, apart_no);
                    add_lease.setString(10, tenant_id);
                    add_lease.setString(11, prop_name);
                    add_lease.setDouble(12, 0);
                    add_lease.executeUpdate();
                    System.out.println("Lease added.");
                    break;
                case 3:
                    
                    System.out.println("Here are all the properties: ");
                    //execute
                    prop_list = get_prop.executeQuery();
                    if(!prop_list.next()){
                        System.out.println("Property not found.");
                        break;
                    }
                    do{
                        System.out.println(prop_list.getString(1));
                    }while(prop_list.next());

                    // record move-out
                    System.out.println("Please enter the property name: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    // check if property name exists
                    check_prop = conn.prepareStatement(
                        "select name from property where name=?");
                    check_prop.setString(1, prop_name);
                    prop_res = check_prop.executeQuery();
                    if(!prop_res.next()){
                        System.out.println("Property not found, enter correct property name.");
                        prop_name = input.nextLine();
                    }


                    check_apart = conn.prepareStatement(
                        "select apart_no from apartment where name=?");
                    check_apart.setString(1, prop_name);
                    apart_res = check_apart.executeQuery();
                    if (!apart_res.next()) {
                        System.out.println("Apartment not found.");
                        break;
                    }
                    System.out.println("These are the apartments in this property: ");
                    do{
                        System.out.println(apart_res.getString(1));
                    }while(apart_res.next());

                    
                    System.out.println("Please enter the apartment number: ");
                    apart_no = input.next();

                    System.out.println("These are the existing tenants: ");
                    System.out.println("ID \tFirst Name \tLast Name");
                    // prepare statement
                    get_tenant = conn.prepareStatement(
                        "select id, first_name, last_name from tenant");
                    //execute
                    tenant_res = get_tenant.executeQuery();
                    if (!tenant_res.next()) {
                        System.out.println("Tenant not found.");
                        break;
                    }
                    do{
                        System.out.println(tenant_res.getString(1) + "\t" + tenant_res.getString(2) + "\t" + tenant_res.getString(3));
                    }while(tenant_res.next());

                    System.out.println("Please enter the tenant ID: ");
                    tenant_id = input.next();
                    // check if tenant id exists
                    PreparedStatement check_tenant = conn.prepareStatement(
                        "select id from tenant where id=?");
                    check_tenant.setString(1, tenant_id);
                    tenant_res = check_tenant.executeQuery();
                    while(!tenant_res.next()){
                        System.out.println("Tenant ID not found, enter correct ID.");
                    }
                    // get lease id
                    PreparedStatement get_lease_id = conn.prepareStatement(
                        "select id from lease where prop_name=? and apart_no=? and tenant_id=?");
                    get_lease_id.setString(1, prop_name);
                    get_lease_id.setString(2, apart_no);
                    get_lease_id.setString(3, tenant_id);
                    ResultSet lease_id_res = get_lease_id.executeQuery();
                    
                    if(!lease_id_res.next()){
                        System.out.println("Lease ID not found, enter correct property name, apartment number, and tenant ID.");
                        break;
                    }
                    lease_id = lease_id_res.getString(1);
                    // delete lease by id
                    // prepare statement
                    PreparedStatement record_moveout = conn.prepareStatement(
                        "delete from lease where id=?");
                    //set values
                    record_moveout.setString(1, lease_id);
                    //execute
                    record_moveout.executeUpdate();
                    System.out.println("Move-out date updated.");
                    break;
                case 4:
                    // add person or pet to a lease
                    System.out.println("Enter 1 to add a person OR 2 to add a pet.");
                    while(!input.hasNextInt()){
                        System.out.println("Please input an integer from 1-2.");
                    }
                    int choice = input.nextInt();
                    switch (choice) {
                        case 1:
                            // add a person
                            System.out.println("These are the existing tenants: ");
                            System.out.println("ID \tFirst Name \tLast Name");
                            // prepare statement
                            get_tenant = conn.prepareStatement(
                                "select id, first_name, last_name from tenant");
                            //execute
                            tenant_res = get_tenant.executeQuery();
                            if (!tenant_res.next()) {
                                System.out.println("Tenant not found.");
                                break;
                            }
                            do{
                                System.out.println(tenant_res.getString(1) + "\t" + tenant_res.getString(2) + "\t" + tenant_res.getString(3));
                            }while(tenant_res.next());

                            System.out.println("To add a roommate, please enter your tenant ID: ");
                            // input.nextLine();
                            tenant_id = input.next();
                            // check if tenant id exists
                            check_tenant = conn.prepareStatement(
                                "select id from tenant where id=?");
                            check_tenant.setString(1, tenant_id);
                            tenant_res = check_tenant.executeQuery();
                            if(tenant_res.next()){
                                tenant_id = tenant_res.getString(1);
                            }else{
                                while(!tenant_res.next()){
                                    System.out.println("Tenant ID not found, enter correct ID.");
                                    tenant_id = input.next();
                                    check_tenant = conn.prepareStatement(
                                    "select id from tenant where id=?");
                                    check_tenant.setString(1, tenant_id);
                                    tenant_res = check_tenant.executeQuery();
                                }
                            }
                            // please enter the roommate to roommate table
                            System.out.println("Please enter the roommate's ID from the existing tenants: ");
                            String roommate_id = input.next();
                            // check if roommate id exists in tenants table
                            PreparedStatement check_roommate = conn.prepareStatement(
                                "select id from tenant where id=?");
                            check_roommate.setString(1, roommate_id);
                            ResultSet roommate_res = check_roommate.executeQuery();
                            while(!roommate_res.next()){
                                System.out.println("Roommate ID not found, enter correct ID.");
                            }
                            // insert roommate to roommate table
                            // prepare statement
                            PreparedStatement add_Roommate = conn.prepareStatement(
                                "insert into Roommate values(?, ?)");
                            //set values
                            add_Roommate.setString(1, tenant_id);
                            add_Roommate.setString(2, roommate_id);
                            //execute
                            add_Roommate.executeUpdate();
                            // add that person to roommate table
                            add_Roommate = conn.prepareStatement(
                                "insert into Roommate values(?, ?)");
                            //set values
                            add_Roommate.setString(1, roommate_id);
                            add_Roommate.setString(2, tenant_id);
                            //execute
                            add_Roommate.executeUpdate();
                            System.out.println("Roommate added.");
                            break;
                        case 2:
                            // add a pet
                            System.out.println("These are the existing tenants: ");
                            System.out.println("ID \tFirst Name \tLast Name");
                            // prepare statement
                            get_tenant = conn.prepareStatement(
                                "select id, first_name, last_name from tenant");
                            //execute
                            tenant_res = get_tenant.executeQuery();
                            if (!tenant_res.next()) {
                                System.out.println("Tenant not found.");
                                break;
                            }
                            do{
                                System.out.println(tenant_res.getString(1) + "\t" + tenant_res.getString(2) + "\t" + tenant_res.getString(3));
                            }while(tenant_res.next());
                            System.out.println("Please enter the tenant ID: ");
                            tenant_id = input.next();
                            // check if tenant id exists
                            check_tenant = conn.prepareStatement(
                                "select id from tenant where id=?");
                            check_tenant.setString(1, tenant_id);
                            tenant_res = check_tenant.executeQuery();
                            while(!tenant_res.next()){
                                System.out.println("Tenant ID not found, enter correct ID.");
                            }
                            // please enter the pet to pet table
                            // get number of entry in pet table
                            PreparedStatement get_pet_num = conn.prepareStatement(
                                "select count(*) from pet");
                            ResultSet pet_num_res = get_pet_num.executeQuery();
                            if(pet_num_res.next()){
                                int pet_num = pet_num_res.getInt(1);
                                pet_num++;
                                String pet_id = "P00" + pet_num;
                                // insert data into pet table
                                // prepare statement
                                PreparedStatement add_Pet = conn.prepareStatement(
                                    "insert into Pet values(?, ?)");
                                //set values
                                add_Pet.setString(1, pet_id);
                                add_Pet.setString(2, tenant_id);
                                //execute
                                add_Pet.executeUpdate();
                                System.out.println("Pet added.");
                            }else{
                                System.out.println("Pet not found.");
                            }
                        
                            break;
                        default:
                            break;
                    }
                
                
                    
                    break;
                case 5:

                    System.out.println("Here are all the properties: ");
                    //execute
                    prop_list = get_prop.executeQuery();
                    if(!prop_list.next()){
                        System.out.println("Property not found.");
                        break;
                    }
                    do{
                        System.out.println(prop_list.getString(1));
                    }while(prop_list.next());

                    System.out.println("Please enter the property name: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    // check if property name exists
                    check_prop = conn.prepareStatement(
                        "select name from property where name=?");
                    check_prop.setString(1, prop_name);
                    prop_res = check_prop.executeQuery();
                    if(prop_res.next()){
                        prop_name = prop_res.getString(1);
                    }else{
                        while(!prop_res.next()){
                            System.out.println("Property not found, enter correct property name.");
                            prop_name = input.next();
                            check_prop = conn.prepareStatement(
                            "select name from property where name=?");
                            check_prop.setString(1, prop_name);
                            prop_res = check_prop.executeQuery();
                        }
                    }

                    check_apart = conn.prepareStatement(
                        "select apart_no from apartment where name=?");
                    check_apart.setString(1, prop_name);
                    apart_res = check_apart.executeQuery();
                    if (!apart_res.next()) {
                        System.out.println("Apartment not found.");
                        break;
                    }
                    System.out.println("These are the apartments in this property: (ex.Southside)");
                    do{
                        System.out.println(apart_res.getString(1));
                    }while(apart_res.next());

                    System.out.println("Please enter the apartment number: (ex. A101)");
                    apart_no = input.next();
                    
                    System.out.println("These are the existing tenants: ");
                    System.out.println("ID \tFirst Name \tLast Name");
                    // prepare statement
                    get_tenant = conn.prepareStatement(
                        "select id, first_name, last_name from tenant");
                    //execute
                    tenant_res = get_tenant.executeQuery();
                    if (!tenant_res.next()) {
                        System.out.println("Tenant not found.");
                        break;
                    }
                    do{
                        System.out.println(tenant_res.getString(1) + "\t" + tenant_res.getString(2) + "\t" + tenant_res.getString(3));
                    }while(tenant_res.next());

                    System.out.println("Please enter the tenant ID: (ex. T00000003)");
                    tenant_id = input.next();
                    // check if tenant id exists
                    check_tenant = conn.prepareStatement(
                        "select id from tenant where id=?");
                    check_tenant.setString(1, tenant_id);
                    tenant_res = check_tenant.executeQuery();
                    while(!tenant_res.next()){
                        System.out.println("Tenant ID not found, enter correct ID.");
                    }
                    // get lease id
                    get_lease_id = conn.prepareStatement(
                        "select id from lease where prop_name=? and apart_no=? and tenant_id=?");
                    get_lease_id.setString(1, prop_name);
                    get_lease_id.setString(2, apart_no);
                    get_lease_id.setString(3, tenant_id);
                    lease_id_res = get_lease_id.executeQuery();
                    if(lease_id_res.next()){
                        lease_id = lease_id_res.getString(1);
                    }else{
                        System.out.println("Lease ID not found, enter correct property name, apartment number, and tenant ID.");
                        break;
                    }

                    
                    // update move-out date in lease table
                    // prepare statement
                    PreparedStatement get_enddate = conn.prepareStatement(
                        "select end_day, amenity_cost from lease where id=?");
                    //set values
                    get_enddate.setString(1, lease_id);
                    //execute
                    ResultSet enddate_res = get_enddate.executeQuery();
                
                    if(enddate_res.next()){
                        prev_end_date = enddate_res.getDate(1);
                        amenity_cost = enddate_res.getDouble(2);
                    }else{
                        System.out.println("Lease not found.");
                    }
                    System.out.println("Previous move-out date: " + prev_end_date);
                    // set move-out date
                    System.out.println("Please enter the new move-out date: (MM-dd-yyyy)");
                    String move_out_day = input.next();
                    //convert string to date
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date moveout_date = dateFormat.parse(move_out_day);
                    java.sql.Date sql_moveout_date = new java.sql.Date(moveout_date.getTime());

                    // prepare statement
                    PreparedStatement set_moveout = conn.prepareStatement(
                        "update lease set end_day=? where id=?");
    
                    //set values
                    set_moveout.setDate(1, sql_moveout_date);
                    set_moveout.setString(2, lease_id);
                    // execute
                    set_moveout.executeUpdate();
                    System.out.println("Move-out date updated.");
                    // update refound
                    // calculate the difference between new move-out date and previous end date round to floor
                    int diff_month = (int) ((sql_moveout_date.getTime() - prev_end_date.getTime()) / (1000 * 60 * 60 * 24 * 30));
                    double refund = diff_month * (base_rent + amenity_cost);
                    // prepare statement
                    PreparedStatement update_refund = conn.prepareStatement(
                        "update lease set refund=? where id=?");
                    //set values
                    update_refund.setDouble(1, refund);
                    update_refund.setString(2, lease_id);
                    // execute
                    update_refund.executeUpdate();
                    System.out.println("Refund updated.");

                    break;
                default:
                    break;
            }
            return;
        }catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Fail to connect, please re-enter your login info: ");
        }
    }

    /*
     * This method is used to display the tenant menu.
     */
    public static int tenant_menu(Scanner input) {
        int choice = 0;
        while (true) 
        {
            System.out.println();
            System.out.println("----------------------------------Tenant Menu----------------------------------");
            System.out.println("Select your action from below:");
            System.out.println("1. check payment status (amount due, if any)");
            System.out.println("2. make rental payment");
            System.out.println("3. update personal data");
            System.out.println("Enter 0 to go back to main menu.");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 0 && choice <= 3) 
                {
                    break;
                }
                System.out.println("Invalid operation. Must be an integer from 0 to 3.");
            }
            else 
            {
                input.nextLine();
                System.out.println("Invalid operation. Must be an integer.");
            }
        }
        return choice;
    }

    /*
     * This method is used to perform the tenant action.
     */
    public static void tenant_action(int action, Connection conn, String tenant_id, Scanner input) {
        System.out.println("in tenant action");
        try{
            double total_rent;
            String prop_name;
            String apart_no;
            String pay_method = "";
            String payment_id;
            String lease_id;
            double paid_amount;
            String currency;
            String card_number;
            String third_party;
            String online_email;
            int no_month;

            switch (action) {
                case 0:
                    break;
                case 1:
                    // check payment status
                    // print all the payments associated with this tenant
                    // prepare statement
                    PreparedStatement get_all_payments = conn.prepareStatement(
                        "select id, prop_name, apart_no from lease where tenant_id=?");
                    //set values
                    get_all_payments.setString(1, tenant_id);
                    ResultSet all_payments_res = get_all_payments.executeQuery();
                    if(!all_payments_res.next()){
                        System.out.println("No lease associated with the tenant.");
                        break;
                    }
                    System.out.println("These are the payments associated with the tenant: ");
                    System.out.println("ID \tProperty Name \tApartment Number");
                    do{
                        System.out.println(all_payments_res.getString(1) + "\t" + all_payments_res.getString(2) + "\t" + all_payments_res.getString(3));
                    }while(all_payments_res.next());

                    //set values
                    System.out.println("To check payment status, please enter the information below");
                    System.out.println("Please enter the property name: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    System.out.println("Please enter the apartment number: ");
                    apart_no = input.next();

                    //prepare statement
                    PreparedStatement get_payment_status = conn.prepareStatement(
                        "select amenity_cost, base_rent, id, no_month,paid_amount from lease where prop_name=? and apart_no=? and tenant_id=?");
                    //set values
                    get_payment_status.setString(1, prop_name);
                    get_payment_status.setString(2, apart_no);
                    get_payment_status.setString(3, tenant_id);
                    //execute
                    ResultSet payment_res = get_payment_status.executeQuery();
                    if(!payment_res.next()){
                        System.out.println("Payment not found. Enter correct information.");
                        break;
                    }
                    lease_id = payment_res.getString(3);
                    no_month = payment_res.getInt(4);
                    paid_amount = payment_res.getDouble(5);
                    total_rent = (payment_res.getDouble(1) + payment_res.getDouble(2))*no_month - paid_amount;
                    System.out.println("Amount: " + total_rent);
                    break;
                case 2:
                    // print all the payments associated with this tenant
                    // prepare statement
                    get_all_payments = conn.prepareStatement(
                        "select id, prop_name, apart_no from lease where tenant_id=?");
                    //set values
                    get_all_payments.setString(1, tenant_id);
                    all_payments_res = get_all_payments.executeQuery();
                    if(!all_payments_res.next()){
                        System.out.println("No lease associated with the tenant.");
                        break;
                    }
                    System.out.println("These are the payments associated with the tenant: ");
                    System.out.println("ID \tProperty Name \tApartment Number");
                    do{
                        System.out.println(all_payments_res.getString(1) + "\t" + all_payments_res.getString(2) + "\t" + all_payments_res.getString(3));
                    }while(all_payments_res.next());

                    // make rental payment
                    System.out.println("To make rental payment, please enter the information below");
                    System.out.println("Please enter the property name: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    System.out.println("Please enter the apartment number: ");
                    apart_no = input.next();

                    //check in lease for amenity_cost and base_rent
                    // prepare statement
                    PreparedStatement get_lease = conn.prepareStatement(
                        "select amenity_cost, base_rent, id, no_month,paid_amount from lease where prop_name=? and apart_no=? and tenant_id=?");
                    //set values
                    get_lease.setString(1, prop_name);
                    get_lease.setString(2, apart_no);
                    get_lease.setString(3, tenant_id);
                    // execute
                    ResultSet lease_res = get_lease.executeQuery();
                    if(!lease_res.next()){
                        System.out.println("Lease not found. Enter correct property name and apartment number.");
                        break;
                    }
                    lease_id = lease_res.getString(3);
                    no_month = lease_res.getInt(4);
                    paid_amount = lease_res.getDouble(5);

                    total_rent = (lease_res.getDouble(1) + lease_res.getDouble(2))*no_month - paid_amount;
                    System.out.println("Total rent left: " + total_rent);
                    System.out.println("Please enter the payment method (1-3): ");
                    System.out.println("1. Online payment");
                    System.out.println("2. Cash");
                    System.out.println("3. Card");
                    int method =-1;
                    System.out.println("Please input an integer from 1-3.");
                    while(true){
                        if (input.hasNextInt()) {
                            method = input.nextInt();
                            if (method >= 0 && method <= 3) 
                            {
                                break;
                            }
                            System.out.println("Invalid operation. Must be an integer from 0 to 3.");
                        }
                        else 
                        {
                            input.nextLine();
                            System.out.println("Invalid operation. Must be an integer.");
                        }
                    }
                    

                    // int method = input.nextInt();
                    switch (method) {
                        case 1:
                            pay_method = "Online payment";
                            break;
                        case 2:
                            pay_method = "Cash";
                            break;
                        case 3:
                            pay_method = "Card";
                            break;
                        default:
                            break;
                    }

                    // insert to payment table
                    //prepare statement
                    PreparedStatement add_Payment = conn.prepareStatement(
                        "insert into Payment values(?, ?, ?, ?, ?, ?)");
                    //set values
                    // generate payment id
                    // get number of entries in payment table
                    PreparedStatement get_payment_num = conn.prepareStatement(
                        "select count(*) from payment");
                    ResultSet payment_num_res = get_payment_num.executeQuery();
                    

                    if(!payment_num_res.next()){
                        System.out.println("Payment not found.");
                    }
                    int payment_num = payment_num_res.getInt(1);


                    payment_num++;

                    // count number of digits in payment_num
                    if(payment_num < 10){
                        payment_id = "P000000000" + payment_num;
                    }else if(payment_num < 100){
                        payment_id = "P00000000" + payment_num;
                    }else if(payment_num < 1000){
                        payment_id = "P0000000" + payment_num;
                    }else if(payment_num < 10000){
                        payment_id = "P000000" + payment_num;
                    }else if(payment_num < 100000){
                        payment_id = "P00000" + payment_num;
                    }else if(payment_num < 1000000){
                        payment_id = "P0000" + payment_num;
                    }else if(payment_num < 10000000){
                        payment_id = "P000" + payment_num;
                    }else if(payment_num < 100000000){
                        payment_id = "P00" + payment_num;
                    }else if(payment_num < 1000000000){
                        payment_id = "P0" + payment_num;
                    }else{
                        payment_id = "P" + payment_num;
                    }

                    add_Payment.setString(1, payment_id);
                    add_Payment.setDouble(2, total_rent);
                    add_Payment.setString(3, pay_method);
                    // get current time
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
                    add_Payment.setTimestamp(4, timestamp);
                    add_Payment.setString(5, lease_id);
                    add_Payment.setString(6, tenant_id);
                    //execute
                    add_Payment.executeUpdate();
                    // System.out.println("[test] Payment row inserted.");


                    switch (method) {
                        case 1:// online payment
                            System.out.println("Please enter the name of online payment party: ");
                            third_party = input.next();
                            System.out.println("Please enter the associated email with your payment party: ");
                            online_email = input.next();
                            System.out.println("Please enter the amount you want to pay: ");
                            while(!input.hasNextDouble()){
                                System.out.println("Please input a number.");
                                break;
                            }
                            paid_amount = input.nextDouble();

                            // insert data into third_party table
                            // prepare statement
                            PreparedStatement add_ThirdPartyPayment = conn.prepareStatement(
                                "insert into Third_Party values(?, ?, ?, ?)");
                            //set values
                            add_ThirdPartyPayment.setString(1, payment_id);
                            add_ThirdPartyPayment.setString(2, third_party);
                            add_ThirdPartyPayment.setString(3, online_email);
                            add_ThirdPartyPayment.setDouble(4, paid_amount);
                            //execute
                            add_ThirdPartyPayment.executeUpdate();
                            System.out.println("Third Party Payment went through.");

                            //prepare statement
                            // update paid amount in lease table by adding paid_amount to current paid_amount
                            PreparedStatement update_paid_amount = conn.prepareStatement(
                                "update lease set paid_amount=paid_amount+? where id=?");
                            //set values
                            update_paid_amount.setDouble(1, paid_amount);
                            update_paid_amount.setString(2, lease_id);
                            //execute
                            update_paid_amount.executeUpdate();
                            break;
                        case 2:// cash
                            System.out.println("Please enter the name of currency: ");
                            currency = input.next();
                            System.out.println("Please enter the amount you want to pay: ");
                            while(!input.hasNextDouble()){
                                System.out.println("Please input a number.");
                                break;
                            }
                            paid_amount = input.nextDouble();

                            // insert data into cash table
                            //prepare statement
                            PreparedStatement add_CashPayment = conn.prepareStatement(
                                "insert into Cash values(?, ?, ?)");
                            //set values
                            add_CashPayment.setString(1, payment_id);
                            add_CashPayment.setString(2, currency);
                            add_CashPayment.setDouble(3, paid_amount);
                            //execute
                            add_CashPayment.executeUpdate();
                            System.out.println("Cash Payment went through.");

                            //prepare statement
                            // update paid amount in lease table by adding paid_amount to current paid_amount
                            update_paid_amount = conn.prepareStatement(
                                "update lease set paid_amount=paid_amount+? where id=?");
                            //set values
                            update_paid_amount.setDouble(1, paid_amount);
                            update_paid_amount.setString(2, lease_id);
                            //execute
                            update_paid_amount.executeUpdate();
                            break;
                        case 3:// card
                            // PAYMENT_ID	VARCHAR2(20 BYTE)
                            // CARD_NUMBER	VARCHAR2(20 BYTE)
                            // PAID_AMOUNT	NUMBER(5,1)
                            System.out.println("Please enter the card number: ");
                            card_number = input.next();
                            System.out.println("Please enter the amount you want to pay: ");
                            while(!input.hasNextDouble()){
                                System.out.println("Please input a number.");
                                break;
                            }
                            paid_amount = input.nextDouble();
                            
                            // insert data into card table
                            //prepare statement
                            PreparedStatement add_CardPayment = conn.prepareStatement(
                                "insert into Card values(?, ?, ?)");
                            //set values
                            add_CardPayment.setString(1, payment_id);
                            add_CardPayment.setDouble(2, paid_amount);
                            add_CardPayment.setString(3, card_number);
                            //execute
                            add_CardPayment.executeUpdate();
                            System.out.println("Card Payment went through.");

                            //prepare statement
                            // update paid amount in lease table by adding paid_amount to current paid_amount
                            update_paid_amount = conn.prepareStatement(
                                "update lease set paid_amount=paid_amount+? where id=?");
                            //set values
                            update_paid_amount.setDouble(1, paid_amount);
                            update_paid_amount.setString(2, lease_id);
                            //execute
                            update_paid_amount.executeUpdate();
                            
                            break;
                        default:
                            break;
                    }

                    break;
                case 3:
                    // update personal data
                    // list all personal data : name, phone, email
                    // prepare statement
                    PreparedStatement get_PersonalData = conn.prepareStatement(
                        "select first_name,last_name, phone, email from tenant where id=?");
                    //set values
                    get_PersonalData.setString(1, tenant_id);
                    //execute
                    ResultSet personal_res = get_PersonalData.executeQuery();
                    if(!personal_res.next()){
                        System.out.println("Tenant not found. Enter correct tenant ID.");
                        break;
                    }
                    String first_name = personal_res.getString(1);
                    String last_name = personal_res.getString(2);
                    String phone = personal_res.getString(3);
                    String email = personal_res.getString(4);
                    System.out.println("Name: " + first_name + " " + last_name);
                    System.out.println("Phone: " + phone);
                    System.out.println("Email: " + email);

                    System.out.println("Which information do you want to update: ");
                    System.out.println("1. Name");
                    System.out.println("2. Phone");
                    System.out.println("3. Email");

                    // Scanner in = new Scanner(System.in);
                    int update_choice = 0;

                    if(!input.hasNextInt()){
                        System.out.println("Please input an integer from 1-3.");
                    }else{
                        update_choice = input.nextInt(); 
                    }
                    if (update_choice < 1 || update_choice > 3) {
                        
                        while(update_choice < 1 || update_choice > 3){
                            System.out.println("Please input an integer from 0-3.");
                            update_choice = input.nextInt();
                        }
                    }
                    
                    switch (update_choice) {
                        case 1:
                            // System.out.println("Please enter your new name: ");
                            String new_name = "";

                            if(input.hasNextLine()){
                                // new_name = input.next();
                                while (new_name.isEmpty()) {
                                    System.out.println("Please enter your new name: ");
                                    input.nextLine();
                                    new_name = input.nextLine();
                                }
                            }else{
                                System.out.println("Please enter your new name: ");
                            }

                            if (new_name == "") {
                                while(new_name == ""){
                                    System.out.println("Please enter your new name: ");
                                    new_name = input.nextLine();
                                }
                            }
            

                            System.out.println("new name: " + new_name);
                            String[] name = new_name.split(" ");
                            int op = -1;
                            while(op != 0){
                                if(name.length != 2){
                                    System.out.println("Please enter your new name in correct format: ");
                                    System.out.println("Example: John Smith");
                                    new_name = input.nextLine();
                                    name = new_name.split(" ");
                                }else{
                                    System.out.println("enter 0 to confirm");
                                    op = input.nextInt();
                                }
                            }
                            String new_first_name = new_name.split(" ")[0];
                            String new_last_name = new_name.split(" ")[1];
                            // prepare statement
                            PreparedStatement update_Name = conn.prepareStatement(
                                "update tenant set first_name=?, last_name=? where id=?");
                            //set values
                            update_Name.setString(1, new_first_name);
                            update_Name.setString(2, new_last_name);
                            update_Name.setString(3, tenant_id);
                            //execute
                            update_Name.executeUpdate();
                            System.out.println("Name updated.");
                            break;
                        case 2:
                            String new_phone = "";
                             if (input.hasNext()) {
                                new_name = input.next();

                                while (new_phone.isEmpty()) {
                                    System.out.println("Please enter your new phone number: ");
                                    new_phone = input.next();
                                }
                            } 
                            if (new_phone == "") {
                                while(new_phone == ""){
                                    System.out.println("Please enter your new phone number: ");
                                    new_phone = input.next();
                                }
                            }

                            // prepare statement
                            PreparedStatement update_Phone = conn.prepareStatement(
                                "update tenant set phone=? where id=?");
                            //set values
                            update_Phone.setString(1, new_phone);
                            update_Phone.setString(2, tenant_id);
                            //execute
                            update_Phone.executeUpdate();
                            System.out.println("Phone number updated.");
                            break;
                        case 3:
                            String new_email = "";
                            if (input.hasNext()) {
                                new_email = input.next();

                                while (new_email.isEmpty()) {
                                    System.out.println("Please enter your new email: ");
                                    new_email = input.next();
                                }
                            }
                            if (new_email == "") {
                                while(new_email == ""){
                                    System.out.println("Please enter your new email: ");
                                    new_email = input.next();
                                }
                            }

                            // prepare statement
                            PreparedStatement update_Email = conn.prepareStatement(
                                "update tenant set email=? where id=?");
                            //set values
                            update_Email.setString(1, new_email);
                            update_Email.setString(2, tenant_id);
                            //execute
                            update_Email.executeUpdate();
                            System.out.println("Email updated.");
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return;
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Fail to connect, please re-enter your login info: ");
        }
    }

    /*
     * This method is used to display the company manager menu.
     */
    public static int company_manager_menu(Scanner input) {
        int choice = 0;
        while (true) 
        {
            System.out.println();
            System.out.println("----------------------------------Company Manager Menu----------------------------------");
            System.out.println("Select your action from below:");
            System.out.println("1. Add a new property");
            System.out.println("(Provide location, amenities, apartments, etc.)");
            System.out.println("2. Generate Data for each apartment");
            System.out.println("(Provide size, bedrooms, bathroom, features, amenities, etc.)");
            System.out.println("Enter 0 to go back to main menu.");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 0 && choice <= 2) 
                {
                    break;
                }
                System.out.println("Invalid operation. Must be an integer from 0 to 2.");
            }
            else 
            {
                input.nextLine();
                System.out.println("Invalid operation. Must be an integer.");
            }
        }
        return choice;
    }

    public static void company_manager_action(int action, Connection conn, Scanner input) {
        String prop_name;
        try{
            switch (action) {
                case 0:
                    break;
                case 1: // add new property
                    // NAME	VARCHAR2(20 BYTE)
                    // ADDRESS	VARCHAR2(100 BYTE)
                    System.out.println("Please enter the new property name: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    System.out.println("Please enter the new property address: ");
                    // input.nextLine();
                    String prop_address = input.nextLine();

                    // list all existing common amenities
                    // prepare statement
                    PreparedStatement get_CommonAmenity = conn.prepareStatement(
                        "select name from common_amenity");
                    //execute
                    ResultSet common_amenity_res = get_CommonAmenity.executeQuery();
                    System.out.println("Here are all existng Common amenities: ");
                    if (!common_amenity_res.next()) {
                        System.out.println("No common amenity found.");
                        break;
                    }
                    do{
                        System.out.println(common_amenity_res.getString(1));
                    }while(common_amenity_res.next());
                    // prepare statement
                    PreparedStatement add_Property = conn.prepareStatement(
                        "insert into property values(?, ?)");
                    //set values
                    add_Property.setString(1, prop_name);
                    add_Property.setString(2, prop_address);
                    //execute
                    add_Property.executeUpdate();

                    System.out.println("Please enter the common amenities you want to add to this property from the above list, separated by comma: ");
                    System.out.println("Example: Swimming Pool,Gym,Spa");
                    String common_amenity = input.nextLine();
                    String[] common_amenity_arr = common_amenity.split(",");
            
                    // insert data into prop_has_amenity table
                    // prepare statement
                    PreparedStatement add_PropHasAmenity = conn.prepareStatement(
                        "insert into prop_has_amenity values(?, ?)");
                    //set values
                    for(int i = 0; i < common_amenity_arr.length; i++){
                        add_PropHasAmenity.setString(1, common_amenity_arr[i]);
                        add_PropHasAmenity.setString(2, prop_name);
                        add_PropHasAmenity.executeUpdate();
                    }
                    System.out.println("New property added. Common amenities added.");
                    
                    break;
                case 2: // generate data for each apartment
                    System.out.println("Here are all the property listed below:");
                    // prepare statement
                    PreparedStatement get_Property = conn.prepareStatement(
                        "select name from property");
                    //execute
                    ResultSet property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("No property found.");
                        break;
                    }
                    do{
                        System.out.println(property_res.getString(1));
                    }while(property_res.next());
                        
                    System.out.println("Please enter the property name you want to add apartment to: ");
                    input.nextLine();
                    prop_name = input.nextLine();
                    // check if property name exists
                    boolean prop_exist = false;
                    while(!prop_exist){
                        PreparedStatement check_prop = conn.prepareStatement(
                            "select name from property where name=?");
                        check_prop.setString(1, prop_name);
                        property_res = check_prop.executeQuery();
                        if(!property_res.next()){
                            System.out.println("Property not found, enter correct property name.");
                            prop_name = input.nextLine();
                        }else{
                            prop_exist = true;
                        }
                    }
                    System.out.println("Please enter the apartment number that you want to add (ex.C101): ");
                    String apart_no = input.next();
                    // check if apart_no already exists
                    boolean apart_exist = false;
                    while(!apart_exist){
                        PreparedStatement check_apart = conn.prepareStatement(
                            "select apart_no from apartment where apart_no=?");
                        check_apart.setString(1, apart_no);
                        ResultSet apart_res = check_apart.executeQuery();
                        if(apart_res.next()){
                            System.out.println("Apartment already exists, enter correct apartment number.");
                            apart_no = input.next();
                        }
                        else{
                            apart_exist = true;
                        }
                    }
                    System.out.println("Please enter the apartment information as below: ");
                    System.out.println("room_size(m^2), bedroom_no, bathroom_no, rent");
                    System.out.println("Example: 800,2,1,1000");
                    input.nextLine();
                    String info = input.nextLine();
                    String[] info_arr = info.split(",");
                    double room_size = Double.parseDouble(info_arr[0]);
                    int bedroom_no = Integer.parseInt(info_arr[1]);
                    int bathroom_no = Integer.parseInt(info_arr[2]);
                    double rent = Double.parseDouble(info_arr[3]);
                    // insert data into apartment table
                    // prepare statement
                    PreparedStatement add_Apartment = conn.prepareStatement(
                        "insert into Apartment values(?, ?, ?, ?, ?, ?)");
                    //set values
                    add_Apartment.setString(1, apart_no);
                    add_Apartment.setDouble(2, room_size);
                    add_Apartment.setInt(3, bedroom_no);
                    add_Apartment.setInt(4, bathroom_no);
                    add_Apartment.setDouble(5, rent);
                    add_Apartment.setString(6, prop_name);
                    //execute
                    add_Apartment.executeUpdate();
                    System.out.println("Apartment added.");
                    break;
                default:
                    break;
            }
            return;
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Fail to connect, please re-enter your login info: ");
        }
    }

    /*
     * This method is used to display the financial manager menu.
     */
    public static int financial_manager_menu(Scanner input) {
        /*
         * collect aggregate data about a property, some set of
        properties, or the entire enterprise. 
        This user also produces financial reports.
         */
        int choice = 0;
        while (true) 
        {
            System.out.println();
            System.out.println("----------------------------------Financial Manager Menu----------------------------------");
            System.out.println("Select your action from below:");
            System.out.println("1. collect aggregate data about a property");
            System.out.println("2. collect aggregate data about some set of properties");
            System.out.println("3. collect aggregate data about the entire enterprise");
            System.out.println("4. produces financial reports.");
            System.out.println("Enter 0 to go back to main menu.");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 0 && choice <= 4) 
                {
                    break;
                }
                System.out.println("Invalid operation. Must be an integer from 0 to 4.");
            }
            else 
            {
                input.nextLine();
                System.out.println("Invalid operation. Must be an integer.");
            }
        }
        return choice;        
    }

    public static void financial_manager_action(int action, Connection conn, Scanner input) {
        String prop_name;
        PreparedStatement get_CommonAmenity;
        ResultSet common_amenity_res;
        PreparedStatement get_Property;
        PreparedStatement get_lease_id;
        ResultSet lease_id_res;
        String prop_address;
        ResultSet property_res;
        String lease_id = "";
        String payment_id = "";
        try{
            switch (action) {
                case 0:
                    break;
                case 1: // collect aggregate data about a property
                    System.out.println("Here are all the property listed below:");
                    // prepare statement
                    get_Property = conn.prepareStatement(
                        "select name from property");
                    //execute
                    property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("No property found.");
                        break;
                    }
                    // print with a number of sequence
                    int i = 1;
                    do{
                        System.out.println(i + ". " + property_res.getString(1));
                        i++;
                    }while(property_res.next());

                    System.out.println("Please enter the property name: ");
                    while (!input.hasNextLine()) {
                        System.out.println("Please enter the property name: ");
                    }
                    input.nextLine();
                    prop_name = input.nextLine();
                    // get the data by property name
                    // prepare statement
                    get_Property = conn.prepareStatement(
                        "select * from property where name=?");
                    //set values
                    get_Property.setString(1, prop_name);
                    //execute
                    property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("Property not found. Enter correct property name.");
                    }
                    prop_address = property_res.getString(2);
                    System.out.println("Property name: " + prop_name);
                    System.out.println("Property address: " + prop_address);
                    // get the common amenities by property name
                    // prepare statement
                    get_CommonAmenity = conn.prepareStatement(
                        "select amenity_name from prop_has_amenity where prop_name=?");
                    //set values
                    get_CommonAmenity.setString(1, prop_name);
                    //execute
                    common_amenity_res = get_CommonAmenity.executeQuery();
                    if(!common_amenity_res.next()){
                        System.out.println("No common amenity found.");
                        break;
                    }
                    System.out.println("Common amenities: ");
                    do{
                        System.out.println(common_amenity_res.getString(1));
                    }
                    while(common_amenity_res.next());

                    System.out.println("If you want to see the financial report about this property, please enter 1, otherwise enter 0: ");
                    int report_choice = input.nextInt();
                    if(report_choice == 1){
                        get_lease_id = conn.prepareStatement(
                            "select id from lease where prop_name=?");
                        //set values
                        get_lease_id.setString(1, prop_name);
                        //execute
                        lease_id_res = get_lease_id.executeQuery();
                        if(!lease_id_res.next()){
                            System.out.println("No lease found in this property.");
                            System.out.println("Total revenue: 0");
                            break;
                        }
                        int total_revenue = 0;
                        do{
                            lease_id = lease_id_res.getString(1);
                            // prepare statement
                            // get payment id
                            PreparedStatement get_payment_id = conn.prepareStatement(
                                "select id,method from payment where lease_id=?");
                            //set values
                            get_payment_id.setString(1, lease_id);
                            //execute
                            ResultSet payment_id_res = get_payment_id.executeQuery();
                            if(!payment_id_res.next()){
                                break;
                            }
                            do{
                                payment_id = payment_id_res.getString(1);
                                String method = payment_id_res.getString(2);
                                if(method.equals("Credit Card")){
                                    method = "Card";
                                }else if(method.equals("Online Payment")){
                                    method = "Third_Party";
                                }else{
                                    method = "Cash";
                                }
                                // prepare statement
                                // get payment amount
                                PreparedStatement get_payment_amount = conn.prepareStatement(
                                    "select paid_amount from " + method + " where payment_id=?");
                                //set values
                                get_payment_amount.setString(1, payment_id);
                                //execute
                                ResultSet payment_amount_res = get_payment_amount.executeQuery();
                                if(!payment_amount_res.next()){
                                    // System.out.println("Payment amount not found.");
                                    break;
                                }
                                do{
                                    double payment_amount = payment_amount_res.getDouble(1);
                                    total_revenue += payment_amount;
                                }while(payment_amount_res.next());
                            }while(payment_id_res.next());
                        }while(lease_id_res.next());
                        System.out.println("Total revenue: " + total_revenue);
                    }else{
                        break;
                    }
                    break;
                case 2: // collect aggregate data about some set of properties
                    System.out.println("Here are all the property listed below:");
                    // prepare statement
                    get_Property = conn.prepareStatement(
                        "select name from property");
                    //execute
                    property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("No property found.");
                        break;
                    }
                    // print with a number of sequence
                    i = 1;
                    do{
                        System.out.println(i + ". " + property_res.getString(1));
                        i++;
                    }while(property_res.next());

                    System.out.println("Please enter the property names, separated by comma: ");
                    System.out.println("Example:Southside,5x10,Sayre House,HST");
                    input.nextLine();
                    while (!input.hasNextLine()) {
                        System.out.println("Please enter the property names, separated by comma: ");
                        System.out.println("Example:Southside,5x10,Sayre House,HST");
                    }
                    String prop_names = input.nextLine();
                    String[] prop_names_arr = prop_names.split(",");

                    // System.out.println();
                    for (String name : prop_names_arr) {
                        System.out.println();
                        // get the data by property name
                        // prepare statement
                        get_Property = conn.prepareStatement(
                            "select * from property where name=?");
                        //set values
                        get_Property.setString(1, name);
                        //execute
                        property_res = get_Property.executeQuery();
                        if(!property_res.next()){
                            System.out.println("Property " + name + " not found. Enter correct property name.");
                            continue;
                        }
                        prop_address = property_res.getString(2);
                        System.out.println("Property name: " + name);
                        System.out.println("Property address: " + prop_address);
                        // get the common amenities by property name
                        // prepare statement
                        get_CommonAmenity = conn.prepareStatement(
                            "select amenity_name from prop_has_amenity where prop_name=?");
                        //set values
                        get_CommonAmenity.setString(1, name);
                        //execute
                        common_amenity_res = get_CommonAmenity.executeQuery();
                        if(!common_amenity_res.next()){
                            System.out.println("No common amenity in this property.");
                            continue;
                        }
                        System.out.println("Common amenities: ");
                        do{
                            System.out.println(common_amenity_res.getString(1));
                        }
                        while(common_amenity_res.next());


                        get_lease_id = conn.prepareStatement(
                            "select id from lease where prop_name=?");
                        //set values
                        get_lease_id.setString(1, name);
                        //execute
                        lease_id_res = get_lease_id.executeQuery();
                        if(!lease_id_res.next()){
                            System.out.println("No lease in this property.");
                            System.out.println("Total revenue: 0");
                            continue;
                        }
                        int total_revenue = 0;
                        do{
                            lease_id = lease_id_res.getString(1);
                            // prepare statement
                            // get payment id
                            PreparedStatement get_payment_id = conn.prepareStatement(
                                "select id,method from payment where lease_id=?");
                            //set values
                            get_payment_id.setString(1, lease_id);
                            //execute
                            ResultSet payment_id_res = get_payment_id.executeQuery();
                            if(!payment_id_res.next()){
                                continue;
                            }
                            do{
                                payment_id = payment_id_res.getString(1);
                                String method = payment_id_res.getString(2);
                                if(method.equals("Credit Card")){
                                    method = "Card";
                                }else if(method.equals("Online Payment")){
                                    method = "Third_Party";
                                }else{
                                    method = "Cash";
                                }
                                // prepare statement
                                // get payment amount
                                PreparedStatement get_payment_amount = conn.prepareStatement(
                                    "select paid_amount from " + method + " where payment_id=?");
                                //set values
                                get_payment_amount.setString(1, payment_id);
                                //execute
                                ResultSet payment_amount_res = get_payment_amount.executeQuery();
                                if(!payment_amount_res.next()){
                                    // System.out.println("Payment amount not found.");
                                    continue;
                                }
                                do{
                                    double payment_amount = payment_amount_res.getDouble(1);
                                    total_revenue += payment_amount;
                                }while(payment_amount_res.next());
                            }while(payment_id_res.next());
                        }while(lease_id_res.next());
                        System.out.println("Total revenue: " + total_revenue);
                        
                    }
                    break;
                case 3: // collect aggregate data about the entire enterprise
                    System.out.println("Here are all the property information listed below:");
                    // prepare statement
                    get_Property = conn.prepareStatement(
                        "select name from property");
                    //execute
                    property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("No property found.");
                        break;
                    }
                    // put the results into a string array
                    ArrayList<String> prop_names_list = new ArrayList<String>();
                    do{
                        prop_names_list.add(property_res.getString(1));
                    }while(property_res.next());
                    // print with a number of sequence
                    i = 1;
                    // interate through arraylist
                    for (String name : prop_names_list) {
                        System.out.println();
                        System.out.println(i + ". " + name);
                        i++;
                        // get the data by property name
                        // prepare statement
                        get_Property = conn.prepareStatement(
                            "select * from property where name=?");
                        //set values
                        get_Property.setString(1, name);
                        //execute
                        property_res = get_Property.executeQuery();
                        if(!property_res.next()){
                            System.out.println("Property " + name + " not found. Enter correct property name.");
                            continue;
                        }
                        prop_address = property_res.getString(2);
                        System.out.println("Property name: " + name);
                        System.out.println("Property address: " + prop_address);
                        // get the common amenities by property name
                        // prepare statement
                        get_CommonAmenity = conn.prepareStatement(
                            "select amenity_name from prop_has_amenity where prop_name=?");
                        //set values
                        get_CommonAmenity.setString(1, name);
                        //execute
                        common_amenity_res = get_CommonAmenity.executeQuery();
                        if(!common_amenity_res.next()){
                            System.out.println("No common amenity in this property.");
                            continue;
                        }
                        System.out.println("Common amenities: ");
                        do{
                            System.out.println(common_amenity_res.getString(1));
                        }
                        while(common_amenity_res.next());


                        get_lease_id = conn.prepareStatement(
                            "select id from lease where prop_name=?");
                        //set values
                        get_lease_id.setString(1, name);
                        //execute
                        lease_id_res = get_lease_id.executeQuery();
                        if(!lease_id_res.next()){
                            System.out.println("No lease in this property.");
                            System.out.println("Total revenue: 0");
                            continue;
                        }
                        int total_revenue = 0;
                        do{
                            lease_id = lease_id_res.getString(1);
                            // prepare statement
                            // get payment id
                            PreparedStatement get_payment_id = conn.prepareStatement(
                                "select id,method from payment where lease_id=?");
                            //set values
                            get_payment_id.setString(1, lease_id);
                            //execute
                            ResultSet payment_id_res = get_payment_id.executeQuery();
                            if(!payment_id_res.next()){
                                continue;
                            }
                            do{
                                payment_id = payment_id_res.getString(1);
                                String method = payment_id_res.getString(2);
                                if(method.equals("Credit Card")){
                                    method = "Card";
                                }else if(method.equals("Online Payment")){
                                    method = "Third_Party";
                                }else{
                                    method = "Cash";
                                }
                                // prepare statement
                                // get payment amount
                                PreparedStatement get_payment_amount = conn.prepareStatement(
                                    "select paid_amount from " + method + " where payment_id=?");
                                //set values
                                get_payment_amount.setString(1, payment_id);
                                //execute
                                ResultSet payment_amount_res = get_payment_amount.executeQuery();
                                if(!payment_amount_res.next()){
                                    // System.out.println("Payment amount not found.");
                                    continue;
                                }
                                do{
                                    double payment_amount = payment_amount_res.getDouble(1);
                                    total_revenue += payment_amount;
                                }while(payment_amount_res.next());
                            }while(payment_id_res.next());
                        }while(lease_id_res.next());
                        System.out.println("Total revenue: " + total_revenue);
                    }
                    
                    break;
                case 4: //produce financial report
                    System.out.println("This is the financial report for the whole enterprise: ");
                    get_Property = conn.prepareStatement(
                        "select name from property");
                    //execute
                    property_res = get_Property.executeQuery();
                    if(!property_res.next()){
                        System.out.println("No property found.");
                        break;
                    }
                    // put the results into a string array
                    prop_names_list = new ArrayList<String>();
                    do{
                        prop_names_list.add(property_res.getString(1));
                    }while(property_res.next());

                    // interate through arraylist
                    int total_revenue = 0;
                    for (String name : prop_names_list) {
                        // get the data by property name
                        // prepare statement
                        get_Property = conn.prepareStatement(
                            "select * from property where name=?");
                        //set values
                        get_Property.setString(1, name);
                        //execute
                        property_res = get_Property.executeQuery();
                        if(!property_res.next()){
                            continue;
                        }
                        prop_address = property_res.getString(2);
                        // get the common amenities by property name
                        // prepare statement
                        get_CommonAmenity = conn.prepareStatement(
                            "select amenity_name from prop_has_amenity where prop_name=?");
                        //set values
                        get_CommonAmenity.setString(1, name);
                        //execute
                        common_amenity_res = get_CommonAmenity.executeQuery();
                        if(!common_amenity_res.next()){
                            continue;
                        }

                        get_lease_id = conn.prepareStatement(
                            "select id from lease where prop_name=?");
                        //set values
                        get_lease_id.setString(1, name);
                        //execute
                        lease_id_res = get_lease_id.executeQuery();
                        if(!lease_id_res.next()){
                            continue;
                        }
                        do{
                            lease_id = lease_id_res.getString(1);
                            // prepare statement
                            // get payment id
                            PreparedStatement get_payment_id = conn.prepareStatement(
                                "select id,method from payment where lease_id=?");
                            //set values
                            get_payment_id.setString(1, lease_id);
                            //execute
                            ResultSet payment_id_res = get_payment_id.executeQuery();
                            if(!payment_id_res.next()){
                                continue;
                            }
                            do{
                                payment_id = payment_id_res.getString(1);
                                String method = payment_id_res.getString(2);
                                if(method.equals("Credit Card")){
                                    method = "Card";
                                }else if(method.equals("Online Payment")){
                                    method = "Third_Party";
                                }else{
                                    method = "Cash";
                                }
                                // prepare statement
                                // get payment amount
                                PreparedStatement get_payment_amount = conn.prepareStatement(
                                    "select paid_amount from " + method + " where payment_id=?");
                                //set values
                                get_payment_amount.setString(1, payment_id);
                                //execute
                                ResultSet payment_amount_res = get_payment_amount.executeQuery();
                                if(!payment_amount_res.next()){
                                    // System.out.println("Payment amount not found.");
                                    continue;
                                }
                                do{
                                    double payment_amount = payment_amount_res.getDouble(1);
                                    total_revenue += payment_amount;
                                }while(payment_amount_res.next());
                            }while(payment_id_res.next());
                        }while(lease_id_res.next());
                    }
                    System.out.println("Total revenue: " + total_revenue);
                    break;
                default:
                    break;
            }
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Fail to connect, please re-enter your login info: ");
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        Scanner input = new Scanner(System.in);
        do{
            try{
                System.out.println("Enter your Oracle ID: ");
                String userID = input.next();
                System.out.println("Enter your password: ");
                String password = input.next();
                conn = DriverManager.getConnection(DB_URL, userID, password);
                System.out.println("Connection now established.");
                Statement stmt = conn.createStatement();
                
                int interface_choice = -1;
                while(interface_choice != 0) {
                    interface_choice = select_interface(input);
                    // switch case
                    int ret;
                    switch (interface_choice) {
                        
                        case 0:
                            System.out.println("Thank you! Bye!");
                            break;
                        case 1:
                            // ------------------- define new "ret" value for each case so they don't conflict ---------------- Shut up senior eng
                            // property manager
                            ret = proterty_manager_menu(input);
                            pm_action(ret, conn, input);
                            break;
                        case 2:
                            // tenant -> input.next() -> input.nextInt() -> input.nextDouble() -> [input.nextLine()] Doesn't work!! why?
                            // Input Line: string 1 ks
                            // input.nextLine()
                            // Whenever, the last scanner operation WAS NOT nextLine() and you want to use nextLine(), you must flush first
                            // https://www.freecodecamp.org/news/java-scanner-nextline-call-gets-skipped-solved/
                            int return_tenant = -1;
                            // input.nextLine();
                            System.out.println("Here are all the tenant information: ");
                            // prepare statement
                            PreparedStatement get_Tenant = conn.prepareStatement(
                                "select id, first_name, last_name from tenant");
                            //execute
                            ResultSet tenant_res = get_Tenant.executeQuery();
                            if(!tenant_res.next()){
                                System.out.println("No tenant found.");
                                break;
                            }
                            System.out.println("ID \t First Name \t Last Name");
                            do{
                                System.out.println(tenant_res.getString(1) + " " + tenant_res.getString(2) + " " + tenant_res.getString(3));
                            }while(tenant_res.next());

                            System.out.println("Please enter your tenant ID: ");
                            String tenant_id = input.next();
                            // check if tenant id exists
                            PreparedStatement check_tenant = conn.prepareStatement("select id from tenant where id=?");
                            check_tenant.setString(1, tenant_id);
                            tenant_res = check_tenant.executeQuery();
                            if(!tenant_res.next()) {
                                System.out.println("Tenant ID not found, enter correct ID.");
                            } else {
                                while(return_tenant != 0) {
                                    return_tenant = tenant_menu(input);
                                    // input.next();
                                    tenant_action(return_tenant, conn, tenant_id, input);
                                }
                            }
                            break;
                        case 3:
                            // company manager
                            int return_cm = -1;
                            return_cm = company_manager_menu(input);
                            company_manager_action(return_cm, conn, input);
                            break;
                        case 4:
                            // financial manager
                            int return_fm = -1;
                            return_fm = financial_manager_menu(input);
                            financial_manager_action(return_fm, conn, input);
                            break;
                        default:
                            break;
                    }
                }
                conn.close();
            }
            catch(SQLException se){
                // se.printStackTrace();
                System.out.println("[Error]: Fail to connect, please re-enter your login info: ");
            }
        }while(conn == null);
    }
}