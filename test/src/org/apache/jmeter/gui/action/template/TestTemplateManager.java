package org.apache.jmeter.gui.action.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.jmeter.junit.JMeterTestCase;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Test TemplateManager Class
 */
public class TestTemplateManager extends JMeterTestCase {

    /**
     * Test a valid templateFile.
     */
    @Test
    public void testTemplateFile() throws IOException, SAXException, ParserConfigurationException {
        File xmlTemplate = new File(this.getClass().getResource("validTemplates.xml").getFile());
        TemplateManager templateManager = TemplateManager.getInstance();
        Map<String, Template> templateMap = templateManager.parseTemplateFile(xmlTemplate);
        assertEquals(3, templateMap.size());
        Template testTemplate = templateMap.get("testTemplateWithParameters");
        assertTrue(testTemplate.isTestPlan());
        assertEquals("testTemplateWithParameters", testTemplate.getName());
        assertEquals("/bin/templates/testTemplate.jmx.fmkr", testTemplate.getFileName());
        assertEquals("Template with parameters", testTemplate.getDescription());
        Map<String, String> testTemplateParameters = testTemplate.getParameters();
        assertEquals("n 1", testTemplateParameters.get("testKey1"));
        assertEquals("n 2", testTemplateParameters.get("testKey2"));
        assertEquals("n 3", testTemplateParameters.get("testKey3"));
        
        testTemplate = templateMap.get("testTemplateNotTestPlan");
        assertFalse(testTemplate.isTestPlan());
        assertEquals("testTemplateNotTestPlan", testTemplate.getName());
        assertEquals("/bin/templates/testTemplateNotTestPlan.jmx", testTemplate.getFileName());
        assertEquals("testTemplateNotTestPlan desc", testTemplate.getDescription());
        assertNull(testTemplate.getParameters());
        
        testTemplate = templateMap.get("testTemplate");
        assertTrue(testTemplate.isTestPlan());
        assertEquals("testTemplate", testTemplate.getName());
        assertEquals("/bin/templates/testTemplate.jmx", testTemplate.getFileName());
        assertEquals("testTemplate desc", testTemplate.getDescription());
        assertNull(testTemplate.getParameters());
        
    }

    /**
     * Check that a wrong xml file throws a FileNotFoundException
     */
    @Test(expected = FileNotFoundException.class)
    public void testInvalidTemplateFile() throws Exception {
        String xmlTemplatePath = "missing.xml";
        File templateFile = new File(xmlTemplatePath);
        TemplateManager templateManager = TemplateManager.getInstance();
        templateManager.parseTemplateFile(templateFile);
    }

    @Test(expected = SAXParseException.class)
    public void testInvalidTemplateXml() throws IOException, SAXException, ParserConfigurationException {
        String xmlTemplatePath = this.getClass().getResource("invalidTemplates.xml").getFile();
        File templateFile = new File(xmlTemplatePath);
        TemplateManager templateManager = TemplateManager.getInstance();
        Map<String, Template> templateMap = templateManager.parseTemplateFile(templateFile);
    }
}
