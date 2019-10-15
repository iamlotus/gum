package jinlo.gum.core.runtime;

import jinlo.gum.core.exception.PluginException;
import jinlo.gum.core.model.BusinessCode;
import jinlo.gum.core.model.BusinessCodeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Construct {@link Plugin} from XML file
 */
public class XmlPluginBuilder implements PluginBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPluginBuilder.class);

    // express config in bean
    static class PluginXMLBean {
        private String name;
        private String description;
        private String businessCodeParser;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBusinessCodeParser() {
            return businessCodeParser;
        }

        public void setBusinessCodeParser(String businessCodeParser) {
            this.businessCodeParser = businessCodeParser;
        }

    }

    public static class PluginImpl implements Plugin {

        private String name;

        private String description;

        private Set<BusinessCode> range;

        private BusinessCodeParser parser;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Set<BusinessCode> range() {
            return range;
        }

        @Override
        public BusinessCodeParser getBusinessCodeParser() {
            return parser;
        }

        public PluginImpl(String name, String description, BusinessCodeParser parser) {
            this.name = name;
            this.description = description;
            this.parser = parser;
            this.range = parser.range();
        }

        @Override
        public String toString() {
            return "Plugin[" + name + ']';
        }
    }

    // for test
    XmlPluginBuilder.PluginXMLBean bean;

    public XmlPluginBuilder(InputStream is) {
        try {
            this.bean = parseXmlBean(is);
        } catch (Exception e) {
            if (e instanceof PluginException) {
                throw (PluginException) e;
            } else {
                throw new PluginException("can not create Plugin", e);
            }
        }
        LOGGER.info("load Plugin {} successfully", bean.getName());
    }


    private XmlPluginBuilder.PluginXMLBean parseXmlBean(InputStream is) throws Exception {
        // USE DOM && XPATH
        // TODO: use SAX for less memory usage

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(is);
        XPath xpath = XPathFactory.newInstance().newXPath();
        XmlPluginBuilder.PluginXMLBean bean = new XmlPluginBuilder.PluginXMLBean();


        NodeList name = (NodeList) xpath.evaluate("/Plugin/name", doc, XPathConstants.NODESET);
        if (name.getLength() < 1) {
            String msg = "must specify name of plugin";
            LOGGER.error(msg);
            throw new PluginException(msg);
        }

        if (name.getLength() > 1) {
            String msg = "only 1 name is allowed in plugin config, found " + name.getLength();
            LOGGER.error(msg);
            throw new PluginException(msg);
        }

        bean.name = name.item(0).getTextContent();

        if (bean.name.isEmpty()) {
            String msg = "find empty plugin name";
            LOGGER.error(msg);
            throw new PluginException(msg);
        }

        NodeList description = (NodeList) xpath.evaluate("/Plugin/description", doc, XPathConstants.NODESET);
        if (description.getLength() > 1) {
            String msg = "only 1 description is allowed plugin config, found " + name.getLength() ;
            LOGGER.error(msg);
            throw new PluginException(msg);
        }

        if (description.getLength()==1){
            bean.description=description.item(0).getTextContent();
        }else{
            // description is not mandatory
            bean.description=null;
        }

        String businessCodeParser = (String) xpath.evaluate("/Plugin/BusinessCodeParser/@class", doc, XPathConstants.STRING);
        if (businessCodeParser == null||businessCodeParser.isEmpty()) {
            String msg = "must specify BusinessCodeParser";
            LOGGER.error(msg);
            throw new PluginException(msg);
        }
        bean.businessCodeParser = businessCodeParser;

        return bean;
    }

    @Override
    public Plugin build(BeanRepository repository) {
        String name=bean.getName();
        String description=bean.getDescription();
        BusinessCodeParser businessCodeParser= repository.getBean(bean.businessCodeParser);
        PluginImpl result=new PluginImpl(name,description,businessCodeParser);
        return result;
    }
}
