package android_project.couldyouhelpmesir;

/**
 * Created by egorsafronov on 00.12.2016
 */

public class Request {

    public String first_name;
    public String second_name;
    public String email;
    public String phone_number;
    public String problem;
    public int type;

    public Request() {};

    public Request(String first_name, String second_name, String email, String phone_number,
                   String problem, int type) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.email = email;
        this.phone_number = phone_number;
        this.problem = problem;
        this.type = type;
    }

}
