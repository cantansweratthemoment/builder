package dependencies;

import java.nio.file.Path;
import java.util.List;

/**
 * Интерфейс для управления зависимостями в системе сборки.
 */
public interface DependencyManagerInterface {

    List<String> loadDependencies(Path pomFile) throws DependencyException;

    boolean validateDependencies(List<String> dependencies);

    void addDependency(Path pomFile, String dependency) throws DependencyException;
}
