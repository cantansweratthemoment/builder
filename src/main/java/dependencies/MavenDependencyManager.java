package dependencies;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация DependencyManager для работы с зависимостями в Maven.
 */
public class MavenDependencyManager implements DependencyManagerInterface {

    @Override
    public List<String> loadDependencies(Path pomFile) throws DependencyException {
        List<String> dependencies = new ArrayList<>();
        try {
            File file = pomFile.toFile();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodes = document.getElementsByTagName("dependency");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
                String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
                String version = element.getElementsByTagName("version").item(0).getTextContent();
                dependencies.add(groupId + ":" + artifactId + ":" + version);
            }
        } catch (Exception e) {
            throw new DependencyException("Ошибка при загрузке зависимостей из pom.xml", e);
        }
        return dependencies;
    }

    @Override
    public boolean validateDependencies(List<String> dependencies) {
        // todo
        return !dependencies.isEmpty();
    }

    @Override
    public void addDependency(Path pomFile, String dependency) throws DependencyException {
        try {
            String[] parts = dependency.split(":");
            if (parts.length != 3) {
                throw new DependencyException("Некорректный формат зависимости. Ожидается: groupId:artifactId:version");
            }

            File file = pomFile.toFile();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);

            Element dependenciesElement = (Element) document.getElementsByTagName("dependencies").item(0);
            if (dependenciesElement == null) {
                throw new DependencyException("Файл pom.xml не содержит секцию <dependencies>");
            }

            Element dependencyElement = document.createElement("dependency");
            Element groupId = document.createElement("groupId");
            groupId.setTextContent(parts[0]);
            Element artifactId = document.createElement("artifactId");
            artifactId.setTextContent(parts[1]);
            Element version = document.createElement("version");
            version.setTextContent(parts[2]);

            dependencyElement.appendChild(groupId);
            dependencyElement.appendChild(artifactId);
            dependencyElement.appendChild(version);
            dependenciesElement.appendChild(dependencyElement);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file));

        } catch (Exception e) {
            throw new DependencyException("Ошибка при добавлении зависимости в pom.xml", e);
        }
    }