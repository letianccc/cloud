package springboot;


public class File {
    String id;
    String name;
    String userId;

    public File(String id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public File(String id, String name) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public String getUser() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
