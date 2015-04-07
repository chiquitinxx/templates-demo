yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    meta(charset: "utf-8")
    meta(name: "viewport", content: "width=device-width, initial-scale=1.0")
    title('Demo')
    ['normalize', 'foundation', 'chartist.min', 'sweet-alert', 'demo'].each {
      link(rel: 'stylesheet', href: "css/${it}.css")
    }
    link(href: 'http://fonts.googleapis.com/css?family=Cabin', rel:'stylesheet', type: 'text/css')
    script(src: 'js/lib/sweet-alert.min.js') {}
    script(src: 'js/lib/chartist.min.js') {}
    script('data-main': 'js/demo', src: 'js/lib/require.js') {}
  }
  body {
    header(class: 'bg-black') {
      h1 'Grooscript books demo'
      p(id: 'actualTime') {
        yield 'Time'
      }
    }
    div(class: 'bookList') {}
    section {
      include template: 'widgets.gtpl'
    }
  }
}