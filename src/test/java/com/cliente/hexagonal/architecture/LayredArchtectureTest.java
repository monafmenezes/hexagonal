package com.cliente.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
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

    @Test
    void applicationCoreShouldNotDependOnSpringCache() {
        noClasses()
                .that().resideInAPackage("..application.core..")
                .should().dependOnClassesThat().resideInAPackage("org.springframework.cache..")
                .because("use cases e domínio devem ser independentes de framework")
                .check(classes);
    }

    @Test
    void applicationCoreShouldNotHaveSpringComponentAnnotations() {
        noClasses()
                .that().resideInAPackage("..application.core..")
                .should().beAnnotatedWith(Component.class)
                .orShould().beAnnotatedWith(Service.class)
                .orShould().beAnnotatedWith(Repository.class)
                .because("use cases e domínio não devem ser gerenciados diretamente pelo Spring")
                .check(classes);
    }

    @Test
    void cacheAnnotationsShouldOnlyBeInAdapters() {
        methods()
                .that().areAnnotatedWith(Cacheable.class)
                .should().beDeclaredInClassesThat().resideInAPackage("..adapters..")
                .because("@Cacheable é infraestrutura e deve ficar nos adapters")
                .check(classes);

        methods()
                .that().areAnnotatedWith(CacheEvict.class)
                .should().beDeclaredInClassesThat().resideInAPackage("..adapters..")
                .because("@CacheEvict é infraestrutura e deve ficar nos adapters")
                .check(classes);
    }

    @Test
    void domainShouldNotDependOnAdapters() {
        noClasses()
                .that().resideInAPackage("..application.core.domain..")
                .should().dependOnClassesThat().resideInAPackage("..adapters..")
                .because("o domínio não pode conhecer os adapters")
                .check(classes);
    }
}
