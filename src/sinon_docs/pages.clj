(ns sinon-docs.pages
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]
            [sinon-docs.core :as sinon]
            [clojure.string :as str]
            [net.cgrand.enlive-html :refer [sniptest]]
            [sinon-docs.highlight :refer [highlight]]))

(defn- current-year []
  (+ 1900 (.getYear (java.util.Date.))))

(defn- page
  ([request title body] (page request title {} body))
  ([request title options body]
     (html5
      [:head
       [:meta {:charset "utf-8"}]
       [:title (str "Sinon.JS - " title)]
       [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/sinon.css")}]]
      [:body {:class (:body-class options)}
       [:div.payoff
        [:h1 [:a {:href "/"} "Sinon.JS"]]
        [:p "Standalone test spies, stubs and mocks for JavaScript.<br>
          No dependencies, works with any unit testing framework."]]
       body
       [:div.aside
        [:p (list "Sinon uses "
                  [:a {:href "http://semver.org/"} "Semantic versioning"]
                  ".")]
        [:p (list "Copyright 2010 - "
                  (current-year)
                  ", "
                  [:a {:href "http://cjohansen.no/"} "Christian Johansen"]
                  ". Released under the "
                  [:a {:href "http://www.opensource.org/licenses/bsd-license.php"} "BSD license"]
                  ".")]]
       [:script {:src (link/file-path request "/scripts/sinon-web.js")}]
       [:script {:src "/releases/sinon.js"}]
       [:script {:src "/releases/sinon-ie.js"}]])))

(defn- insert-download-button [html current-release]
  (let [version (:version current-release)
        date (:date current-release)]
    (str/replace html "<div class=\"download-link-placeholder\"/>" (str "<div class=\"content\">
        <div class=\"button\"><a href=\"releases/sinon-" version ".js\">Download Sinon.JS " version "</a></div>
        <p>
          " date " - <a href=\"Changelog.txt\">Changelog</a> - <a href=\"/download/\">More builds/versions</a>
        </p>
      </div>"))))

(defn- highlight-code-blocks [html]
  (sniptest html [:pre.code-snippet] (fn [node] (highlight node {:class "codehilite"}))))

(defn frontpage [context]
  (page
   context
   "Documentation"
   {:body-class "front"}
   (-> (slurp "resources/partials/index.html")
       (insert-download-button (sinon/current-release))
       (highlight-code-blocks))))

(defn get-pages []
  (merge {"/index.html" frontpage}))
