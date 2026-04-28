import java.util.Date;

public class Video extends Content {

    private String url;
    private boolean watched;

    public Video(int id, String name, String description, String url, boolean watched,
                 int skillTreeNodeId, Date createdAt, Date updatedAt) {
        super(id, name, description, skillTreeNodeId, createdAt, updatedAt);
        this.url = url;
        this.watched = watched;
    }

    public String getUrl() { return url; }
    public boolean isWatched() { return watched; }

    @Override
    public void display() {
        System.out.println("Exibindo vídeo: " + getName() + " | URL: " + url);
    }
    public void play() {
        System.out.println("Abrindo vídeo '" + getName() + "' na URL: " + url);
    }

    public void markAsWatched() {
        this.watched = true;
        System.out.println("Vídeo '" + getName() + "' marcado como assistido.");
    }

    public void unmarkAsWatched() {
        this.watched = false;
        System.out.println("Vídeo '" + getName() + "' desmarcado como assistido.");
    }

    public void getProgress() {
        System.out.println("Retornaria o progresso atual de visualização do vídeo pelo usuário.");
    }
}