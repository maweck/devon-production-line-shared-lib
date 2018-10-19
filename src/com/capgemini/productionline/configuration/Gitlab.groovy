package com.capgemini.productionline.configuration.gitlab

/**
 * Contains the configuration methods of the gitlab component
 * <p>
 *     The main purpose collecting configuration methods.
 *
 * Created by tlohmann on 19.10.2018.
 */

class GitlabConfiguration implements Serializable {

  def String accesstoken = ""

  GitlabConfiguration (context) {
    this.context = context
  }

  public int getGroupId (String groupname) {
    def searchresult=this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'GET', url: 'http://gitlab-core/gitlab/api/v4/groups?name='+groupname
    def jsonObject = this.context.readJSON text: searchresult.getContent()
    return jsonObject.id
  } 

  public int getProjectId (String groupname, String projectname) {
    def groupid=getGroupid(groupname)
    def searchresult=this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'GET', url: 'http://gitlab-core/gitlab/api/v4/groups/'+groupid+'/projects?name='+projectname
    def jsonObject = this.context.readJSON text: searchresult.getContent()
    return jsonObject.id
  } 

  public createGroup(String groupname, String grouppath, String groupdesc, String grouptype) {
    this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'POST', url: 'http://gitlab-core/gitlab/api/v4/groups?name='+groupname+'&path='+grouppath+'&description='+groupdesc+'&visibility='+grouptype
  }

  public createProject(String groupname, String projectname, String projectpath, String projectdescription, String branchname) {
    groupid=getGroupId(groupname)
    // create project in target group
    this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'POST', url: 'http://gitlab-core/gitlab/api/v4/projects?name='+projectname+'&path='+projectpath+'&namespace_id='+groupid+'&default_branch='+branchname+'description='+projectdescription
  }

  protectBranch(String group, String project, String branchname) {
    projectid=getProjectId(groupname)
    this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'PUT', url: 'http://gitlab-core/gitlab/api/v4/projects/'+projectid+'/repository/branches/'+branchname+'/protect'
  }

  unprotectBranch(String group, String project, String branchname) {
    projectid=getProjectId(groupname)
    this.context.httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: accesstoken]], httpMode: 'PUT', url: 'http://gitlab-core/gitlab/api/v4/projects/'+projectid+'/repository/branches/'+branchname+'/unprotect'
  }
}