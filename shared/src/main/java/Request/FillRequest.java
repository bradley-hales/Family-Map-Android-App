package Request;

public class FillRequest {
    /**
     * Creates a fill request for
     * the specified username and number of generations
     */
    String username;
//    Integer generations;
    String generations;
    /**
     *
     * @param uname String of desired user
     * @param gen number of generations to fill
     */
    public FillRequest(String uname, String gen){
        username = uname;
        generations = gen;
    }

    /**
     * If no generations are specified, set to default
     * @param uname string name of user
     */
    public FillRequest(String uname){
        username = uname;
        generations = "4";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGenerations() {
        return generations;
    }

    public void setGenerations(String generations) {
        this.generations = generations;
    }
}
