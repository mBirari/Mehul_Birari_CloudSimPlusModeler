package com.cloudsimmodeler.project;

import cloudreports.models.*;
import com.cloudsimmodeler.project.beans.FormDataBean;
import com.cloudsimmodeler.project.beans.OutputBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.cloudsimmodeler.project.CloudSimRunner.cloudAutomationRunner;


public class SimulationServiceImpl implements SimulationService {

    public List<OutputBean> generate(FormDataBean formDataBean, String fileName) throws IOException {

        logger.info("generate() method | start");

        CustomerRegistry customerRegistry = new CustomerRegistry();
        customerRegistry.setCloudlets(formDataBean.getCloudletRegistryList());
        customerRegistry.setVms(formDataBean.getVmRegistryList());

        formDataBean.getCustomerRegistryList().add(customerRegistry);
        String yamlString = yamlGenerate(formDataBean);

        logger.info("generate() method | end");
        return cloudAutomationRunner(yamlString,fileName);
    }

    public String yamlGenerate(FormDataBean formDataBean) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yamlDatacenter = "datacenters:\n" + mapper.writeValueAsString(formDataBean.getDatacenterRegistryList());
        String yamlCustomer = "\ncustomers:\n" + mapper.writeValueAsString(formDataBean.getCustomerRegistryList());

        try {
            FileWriter myWriter = new FileWriter("output.yml");
            myWriter.write(yamlDatacenter + yamlCustomer);
            myWriter.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return yamlDatacenter+yamlCustomer;
    }
}
