package com.cliente.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class LayredArchtectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.cliente.hexagonal");

    @Test
    void layered_architecture_test() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Config").definedBy("com.cliente.hexagonal.config..")
                .layer("Adapters").definedBy("com.cliente.hexagonal.adapters..")
                .layer("Application").definedBy("com.cliente.hexagonal.application..")
                .whereLayer("Config").mayNotBeAccessedByAnyLayer()
                .whereLayer("Adapters").mayOnlyBeAccessedByLayers("Config")
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapters", "Config")
                .check(classes);
    }
}
