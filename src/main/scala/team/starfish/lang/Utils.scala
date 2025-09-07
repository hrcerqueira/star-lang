package team.starfish.lang

import scala.jdk.CollectionConverters._

extension[A] (option: Option[A])
  
  def tap(block: A => Unit) =  
    option match 
      case Some(a) => block(a) 
      case None =>
    option
      
    
extension[T] (obj: T)
  def andThen[R](block: T => R): R =
    block(obj)
    
  def also(block: T => Unit): T =
    block(obj)
    obj
    
extension (line: String)
  def utf8Chars: List[String] =
    line.codePoints()
      .mapToObj(Character.toChars)
      .map[String](new String(_))
      .toList
      .asScala
      .toList