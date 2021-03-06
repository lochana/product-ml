/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.ml.project.mgt;

import java.util.List;

import org.wso2.carbon.ml.project.mgt.dto.Project;
import org.wso2.carbon.ml.project.mgt.exceptions.ProjectManagementServiceException;

/**
 * Class contains services related to project and workflow management
 */
public interface ProjectManagementService {
	
	/**
	 * Creates a new project.
	 *
	 * @param projectID        Unique identifier of the project.
	 * @param projectName      Name of the project.
	 * @param description      Description of the project.
	 * @throws                 ProjectManagementServiceException
	 */
	public void createProject(String projectID, String projectName, String description)
			throws ProjectManagementServiceException;
	
	/**
	 * Delete details of a given project.
	 *
	 * @param projectId    Unique identifier of the project
	 * @throws             ProjectManagementServiceException
	 */
	public void deleteProject(String projectId) throws ProjectManagementServiceException;

	/**
	 * Get the project names and created dates, that a tenant is assigned to.
	 *
	 * @param tenantID     Unique identifier of the tenant
	 * @return             An array of project ID, Name and the created date of the projects associated with a given tenant
	 * @throws             ProjectManagementServiceException
	 */
	public String[][] getTenantProjects(String tenantID) throws ProjectManagementServiceException;

	/**
	 * Returns the ID of the data-set associated with the project.
	 *
	 * @param projectId    Unique identifier of the project
	 * @return             Unique identifier of the data-set associated with the project
	 * @throws             ProjectManagementServiceException
	 */
	public String getdatasetID(String projectId) throws ProjectManagementServiceException;

	/**
	 * Create a new machine learning workflow.
	 *
	 * @param workflowID           Unique identifier for the new workflow.
	 * @param parentWorkflowID     Unique identifier for the workflow from which the current workflow is inherited from.
	 * @param projectID            Unique identifier for the project for which the workflow is created.
	 * @param workflowName         Name of the project
	 * @throws                     ProjectManagementServiceException
	 */
	public void createNewWorkflow(String workflowID, String parentWorkflowID, String projectID,
	                              String workflowName) throws ProjectManagementServiceException;

	/**
	 * This method update the workflow name associated with given workflowID
	 * 
	 * @param workflowID   Unique Identifier of this workflow
	 * @param name         Updated name of the workflow
	 * @throws             ProjectManagementServiceException
	 */
	public void updateWorkflowName(String workflowID, String name)
			                        throws ProjectManagementServiceException;

	/**
	 * Delete an existing workflow.
	 *
	 * @param workflowID   Unique identifier of the workflow to be deleted
	 * @throws             ProjectManagementServiceException
	 */
	public void deleteWorkflow(String workflowID) throws ProjectManagementServiceException;

	/**
	 * Get the array of workflows in a project.
	 *
	 * @param projectId    Unique identifier for the project for which the wokflows are needed.
	 * @return             An array of workflow ID's and Names
	 * @throws             ProjectManagementServiceException
	 */
	public String[][] getProjectWorkflows(String projectId)
			throws ProjectManagementServiceException;

	/**
	 * Set the default values for feature properties, of a given workflow.
	 *
	 * @param workflowID   Unique Identifier of the new workflow
	 * @param datasetID    Unique Identifier of the data-set associated with the workflow
	 * @throws             DatasetServiceException
	 */
	public void setDefaultFeatureSettings(String projectID, String workflowID)
			throws ProjectManagementServiceException;

	/**
	 * Get all the project associated with a tenant.
	 * 
	 * @param tenantId     Unique identifier of the tenant.
	 * @return             List of procets.
	 * @throws             ProjectManagementServiceException
	 */
	public List<Project> getAllProjects(String tenantId) throws ProjectManagementServiceException;

	/**
	 * Send an email notification.
	 * 
	 * @param operation
	 * @param username
	 */
    void sendNotification(String operation, String username);
}
