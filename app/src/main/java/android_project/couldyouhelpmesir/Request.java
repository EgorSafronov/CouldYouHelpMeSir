package android_project.couldyouhelpmesir;

/**
 * Created by egorsafronov on 00.12.2016
 */

public class Request {

    public String first_name;
    public String second_name;
    public String problem;

    public Request() {};

    public Request(String first_name, String second_name, String problem) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.problem = problem;
    }

}
