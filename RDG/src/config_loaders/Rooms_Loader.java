package config_loaders;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import config_templates.Room_Template;

public class Rooms_Loader {

	private String configpath;
	private Map<String, Room_Template> room_templates;
	
	public Rooms_Loader(String configpath) {
		this.configpath = configpath;
		room_templates = new HashMap<String, Room_Template>();
	}
	
	/**
	 * Tries to parse XML Config File
	 * Checks if values from config are valid and throw error on failure
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Map<String, Room_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		//implement proper error handling
		
		try {
			
			File fXmlFile = new File(configpath + "Rooms.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Room");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					//allocate memory and store current element's values
					String name, description, image;
 					float item_multiplier;
 					int item_count;
 					Map<String, Float> monster, find_probabilities;
 					boolean[] door_positions; //0: N, 1: E, 2: S, 3: W
 
 					monster = new HashMap<String, Float>();
 					find_probabilities = new HashMap<String, Float>();
 					door_positions = new boolean[4];
					
					name = eElement.getElementsByTagName("Name").item(0).getTextContent();
					description = eElement.getElementsByTagName("Description").item(0).getTextContent();
					image = eElement.getElementsByTagName("Image").item(0).getTextContent();
					item_multiplier = Float.parseFloat(eElement.getElementsByTagName("Item_Multiplier").item(0).getTextContent());
					item_count = Integer.parseInt(eElement.getElementsByTagName("Item_Count").item(0).getTextContent());
					
					//check if parsed values are valid and set enums
					
					
					//process elements on lower hierarchy levels
					
						//read monster placing probabilities
						NodeList monster_list = (NodeList) eElement.getElementsByTagName("Monster").item(0);
						int monster_length = monster_list.getLength();
						
						for (int monster_temp = 0; monster_temp < monster_length; monster_temp++) {
							Node monster_node = monster_list.item(monster_temp);
							if (monster_node.getNodeType() == Node.ELEMENT_NODE) {
								
								
								monster.put(monster_node.getNodeName(),  Float.parseFloat(monster_node.getTextContent()));
							}
						}
						
						//read item finding probabilities
						NodeList find_prob_list = (NodeList) eElement.getElementsByTagName("Find_Probabilities").item(0);
						int find_prob_length = find_prob_list.getLength();
						
						for (int find_prob_temp = 0; find_prob_temp < find_prob_length; find_prob_temp++) {
							Node find_prob_node = find_prob_list.item(find_prob_temp);
							if (find_prob_node.getNodeType() == Node.ELEMENT_NODE) {
								
								
								find_probabilities.put(find_prob_node.getNodeName(),  Float.parseFloat(find_prob_node.getTextContent()));
							}
						}
						
						//read default door positions
						int n = 0, e = 0, s = 0, w = 0;
						
						NodeList door_pos_list = (NodeList) eElement.getElementsByTagName("Door_Positions").item(0);
						int door_pos_length = door_pos_list.getLength();
						
						for (int door_pos_temp = 0; door_pos_temp < door_pos_length; door_pos_temp++) {
							Node door_pos_node = door_pos_list.item(door_pos_temp);
							if (door_pos_node.getNodeType() == Node.ELEMENT_NODE) {
															
							
								if (door_pos_node.getNodeName().equals("N")) {
									n = Integer.parseInt(door_pos_node.getTextContent());
								}
								else if (door_pos_node.getNodeName().equals("E")) {
									e = Integer.parseInt(door_pos_node.getTextContent());
								}
								else if (door_pos_node.getNodeName().equals("S")) {
									s = Integer.parseInt(door_pos_node.getTextContent());
								}
								else if (door_pos_node.getNodeName().equals("W")) {
									w = Integer.parseInt(door_pos_node.getTextContent());
								}
								else {
									//error
								}
							}
						}
						
						door_positions[0] = n == 1;
						door_positions[1] = e == 1;
						door_positions[2] = s == 1;
						door_positions[3] = w == 1;	
						
						//put template on list of available templates
						room_templates.put(name, new Room_Template(name, description, image, item_multiplier, item_count, 
								monster, find_probabilities, door_positions));
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
		
		return room_templates;
	}
}
