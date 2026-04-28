import java.util.Date;

public class Question extends Content {

    private String[] alternatives;
    private String correctAnswer;
    private String selectedAnswer;

    public Question(int id, String name, String[] alternatives, String correctAnswer,
                    String selectedAnswer, int skillTreeNodeId, Date createdAt, Date updatedAt) {
        super(id, name, null, skillTreeNodeId, createdAt, updatedAt); // description não se aplica diretamente
        this.alternatives = alternatives;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = selectedAnswer;
    }

    public String[] getAlternatives() { return alternatives; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getSelectedAnswer() { return selectedAnswer; }

    @Override
    public void display() {
        System.out.println("Exibindo questão: " + getName());
        displayAlternatives();
    }

    public void answer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
        System.out.println("Alternativa '" + selectedAnswer + "' salva para a questão '" + getName() + "'.");
    }

    public void checkAnswer() {
        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            System.out.println("Resposta correta!");
        } else {
            System.out.println("Resposta incorreta. A correta era: " + correctAnswer);
        }
    }

    public void resetAnswer() {
        this.selectedAnswer = null;
        System.out.println("Resposta da questão '" + getName() + "' foi resetada.");
    }

    public void displayAlternatives() {
        System.out.println("Alternativas disponíveis:");
        for (String alt : alternatives) {
            System.out.println("  - " + alt);
        }
    }
}