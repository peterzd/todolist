package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.Task

object Application extends Controller {
  
  val taskForm = Form("label" -> nonEmptyText)
  
  def index = Action {
    Redirect(routes.Application.tasks)
  }
  
  def tasks = Action{
    Ok(views.html.index(Task.all() , taskForm))
  } 
  
  def newTask = Action{ implicit request =>
    taskForm.bindFromRequest.fold(//用 bindFromRequest来创建一个填满数据的request，则在Scope中可以使用Request
        errors => BadRequest(views.html.index(Task.all , errors)), 
        label => {
          Task.create(label)
          Redirect(routes.Application.tasks)
        })
    
  }
  
  def deleteTask(id : Long) = Action{
    Task.delete(id)
    Redirect(routes.Application.tasks)
  } 
  
}