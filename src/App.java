import java.util.Date;

public class App {
    public static void main(String[] args) {

        Date now = new Date();

        System.out.println("===== TESTE: Video =====");

        Video video = new Video(
            1,
            "Introdução ao Mercado Financeiro",
            "Conceitos básicos de finanças",
            "https://fintech.com/videos/intro",
            false,
            10,
            now,
            now
        );

        video.create();
        video.display();

        video.play();
        video.markAsWatched();
        System.out.println("Assistido: " + video.isWatched());
        video.unmarkAsWatched();
        video.delete();

        System.out.println();

        System.out.println("===== TESTE: Question =====");

        String[] alternatives = {"Renda Fixa", "Renda Variável", "Tesouro Direto", "CDI"};

        Question question = new Question(
            1,
            "O que é um investimento de baixo risco?",
            alternatives,
            "Renda Fixa",
            null,
            10,
            now,
            now
        );


        question.create();
        question.display();

        question.answer("Renda Variável");
        question.checkAnswer();   // resposta errada
        question.resetAnswer();
        question.answer("Renda Fixa");
        question.checkAnswer();   // resposta certa
        question.delete();

        System.out.println();

        System.out.println("===== POLIMORFISMO: Content[] =====");

        Content[] conteudos = { video, question };
        for (Content c : conteudos) {
            System.out.println("Conteúdo ID " + c.getId() + ": " + c.getName());
            c.display(); // cada um executa seu próprio display()
            System.out.println();
        }
    }
}