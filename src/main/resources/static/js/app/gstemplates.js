function Templates() {
  var gSobject = gs.inherit(gs.baseClass,'Templates');
  gSobject.clazz = { name: 'org.grooscript.gradle.template.Templates', simpleName: 'Templates'};
  gSobject.clazz.superclass = { name: 'java.lang.Object', simpleName: 'Object'};
  gSobject.__defineGetter__('templates', function(){ return Templates.templates; });
  gSobject.__defineSetter__('templates', function(gSval){ Templates.templates = gSval; });
  gSobject.applyTemplate = function(x0,x1) { return Templates.applyTemplate(x0,x1); }
  if (arguments.length == 1) {gs.passMapToObject(arguments[0],gSobject);};
  
  return gSobject;
};
Templates.applyTemplate = function(name, model) {
  if (model === undefined) model = gs.map();
  var cl = Templates.templates [ name];
  gs.sp(cl,"delegate",model);
  return (cl.delegate!=undefined?gs.applyDelegate(cl,cl.delegate,[model]):gs.execCall(cl, this, [model]));
}
Templates.templates = gs.map().add("index.tpl",function(model) {
  if (model === undefined) model = gs.map();
  return gs.mc(HtmlBuilder,"build",[function(it) {
    gs.mc(Templates,"yieldUnescaped",["<!DOCTYPE html>"]);
    return gs.mc(Templates,"html",[function(it) {
      gs.mc(Templates,"head",[function(it) {
        gs.mc(Templates,"title",["Book Store Demo"]);
        gs.mc(Templates,"link",[gs.map().add("rel","stylesheet").add("href","css/demo.css")]);
        return gs.mc(Templates,"script",[gs.map().add("data-main","js/demo").add("src","js/lib/require.js"), function(it) {
        }]);
      }]);
      return gs.mc(Templates,"body",[function(it) {
        gs.mc(Templates,"header",[gs.map().add("class","bg-black"), function(it) {
          return gs.mc(Templates,"h1",["Book Store"]);
        }]);
        return gs.mc(Templates,"section",[function(it) {
          return gs.mc(Templates,"ul",[function(it) {
            return gs.mc(gs.fs('books', this),"each",[function(book) {
              return gs.mc(Templates,"yieldUnescaped",[gs.execStatic(Templates,'applyTemplate', this,["bookList.gtpl", model])]);
            }]);
          }]);
        }]);
      }]);
    }]);
  }]);
});
