package fr.kbertet.context.io;

/*
 * ContextWriterCsv.java
 *
 * Copyright: 2010-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import java.util.TreeSet;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import fr.kbertet.io.Writer;
import fr.kbertet.context.Context;

/**
 * This class defines the way for writing a context as a text file.
 *
 * ![ContextWriterCsv](ContextWriterCsv.png)
 *
 * @uml ContextWriterCsv.png
 * !include resources/fr/kbertet/context/io/ContextWriterCsv.iuml
 * !include resources/fr/kbertet/io/Writer.iuml
 *
 * hide members
 * show ContextWriterCsv members
 * class ContextWriterCsv #LightCyan
 * title ContextWriterCsv UML graph
 */
public final class ContextWriterCsv implements Writer<Context> {
    /**
     * This class is not designed to be publicly instantiated.
     */
    private ContextWriterCsv() {
    }

    /**
     * The singleton instance.
     */
    private static ContextWriterCsv instance = null;

    /**
     * Return the singleton instance of this class.
     *
     * @return  the singleton instance
     */
    public static ContextWriterCsv getInstance() {
        if (instance == null) {
            instance = new ContextWriterCsv();
        }
        return instance;
    }

    /**
     * Register this class for writing .csv files.
     */
    public static void register() {
        ContextWriterFactory.register(ContextWriterCsv.getInstance(), "csv");
    }

    /**
     * Write a context to a csv file.
     *
     * The following format is respected:
     *
     * The first line contains the attribute names, the other lines contains the observations identifier followed by boolean values
     *
     * ~~~
     * "",a,b,c,d,e
     * 1,1,0,1,0,0
     * 2,1,1,0,0,0
     * 3,0,1,0,1,1
     * 4,0,0,1,0,1
     * ~~~
     *
     * @param   context  a context to write
     * @param   file     a file
     *
     * @throws  IOException  When an IOException occurs
     */
    public void write(Context context, BufferedWriter file) throws IOException {
        CSVPrinter printer = new CSVPrinter(file, CSVFormat.RFC4180);

        // Get the observations and the attributes
        TreeSet<Comparable> observations = context.getObservations();
        TreeSet<Comparable> attributes = context.getAttributes();

        // Prepare the attribute line
        printer.print("");

        for (Comparable attribute : attributes) {
            // Write each attribute
            printer.print(attribute);
        }

        printer.println();

        for (Comparable observation : observations) {
            // Write the observation
            printer.print(observation);

            // Write the extent/intents
            for (Comparable attribute : attributes) {
                if (context.getIntent(observation).contains(attribute)) {
                    printer.print(1);
                } else {
                    printer.print(0);
                }
            }

            printer.println();
        }

        printer.close();
    }
}
