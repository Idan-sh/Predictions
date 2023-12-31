package com.idansh.engine.jaxb.unmarshal.reader;

import com.idansh.engine.jaxb.unmarshal.converter.Converter;
import com.idansh.engine.world.World;
import com.idansh.engine.jaxb.schema.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Class that Converts data generated from an XML file to the project classes,
 * which the simulation can work with.
 */
public class Reader {
    /**
     * Converts a PRD-World with its data to a World class and returns it.
     * Reads the XML world's data into an intermediate class from the given schema,
     * and converts the intermediate object to a World object which the simulation can use.
     * @param file XML file containing world data.
     * @return World data class object.
     */
    public static World readWorld(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            PRDWorld prdWorld = deserializeXML(inputStream);

            return Converter.worldConvert(prdWorld);
        } catch (FileNotFoundException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given an XML file as an input stream, reads it into the PRDWorld class generated from the scheme.
     * @param inputStream the XML file that contains the world data.
     * @return PRDWorld object read from the XML file.
     */
    private static PRDWorld deserializeXML(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PRDWorld) unmarshaller.unmarshal(inputStream);
    }
}
