package com.cloudsimmodeler.project.controller;

import com.cloudsimmodeler.project.*;
import com.cloudsimmodeler.project.beans.DataCenterBean;
import com.cloudsimmodeler.project.beans.FormDataBean;
import com.cloudsimmodeler.project.beans.OutputBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;


@Controller
public class HomeController {
    private final SimulationService simulationService = new SimulationServiceImpl();
    FormDataBean yamlDataBean = new FormDataBean();
    static Logger logger  = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView start(){
        return new ModelAndView("index");
    }


    @RequestMapping(value = "/sendDataVmCloudlet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<OutputBean> sendDataVmCloudlet(@RequestBody FormDataBean dataBean) throws  IOException {
        logger.info("Inside sendDataVmCloudlet method | start");
        yamlDataBean.setVmRegistryList(dataBean.getVmRegistryList());
        yamlDataBean.setCloudletRegistryList(dataBean.getCloudletRegistryList());

        List<OutputBean> output = simulationService.generate(yamlDataBean, "output.yml");
        logger.info("Inside sendDataVmCloudlet method | end");
        return output;
    }

    @RequestMapping(value = "/sendDataDC", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody void sendDataDC(@RequestBody DataCenterBean dataBean){
        logger.info("Inside sendDataDC method | start");
        dataBean.getDatacenterRegistry().setHosts(dataBean.getHostRegistryList());
        yamlDataBean.getDatacenterRegistryList().add(dataBean.getDatacenterRegistry());
        logger.info("Inside sendDataDC method | end");
    }

}
