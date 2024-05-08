import java.time.LocalDate;
import java.util.Map;

public abstract class Report {
    protected Map<Genre, Section> sections;

    public Report(Map<Genre, Section> sections) {
        this.sections = sections;
    }

    public abstract void generateReport();
}