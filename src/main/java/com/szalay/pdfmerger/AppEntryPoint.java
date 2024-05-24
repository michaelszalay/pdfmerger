package com.szalay.pdfmerger;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class AppEntryPoint {

    public static void main(String... args) throws IOException {
        final PDFMergerUtility merger = new PDFMergerUtility();
        final String rootDir = args[0];
        System.out.println("Merging all pdfs from root " + rootDir);

        //Walk through directory and merge all pdfds...
        Files.walkFileTree(new File(rootDir).toPath(), new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("Checking " + file.getFileName());

                if (file.getFileName().toString().trim().endsWith("pdf")) {
                    System.out.println("Adding file " + file.getFileName());
                    merger.addSource(file.toFile());
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        merger.setDestinationFileName("merged.pdf");

        final MemoryUsageSetting settings = MemoryUsageSetting.setupMainMemoryOnly();
        merger.mergeDocuments(settings);
        System.out.println("Finished.");
    }
}


