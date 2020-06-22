(ns wh.landing-new.events
  (:require [wh.graphql-cache :refer [reg-query]]
            [wh.components.activities.queries :as activities-queries]
            #?(:cljs [wh.pages.core :refer [on-page-load]])
            [wh.graphql.fragments])
  (#?(:clj :require :cljs :require-macros)
    [wh.graphql-macros :refer [defquery]]))

(reg-query :all-activities activities-queries/all-activities-query)

(defn all-activities [_db]
  ;; TODO [ch4393] use (get-in db [:wh/query-params "tags"]) to get list of
  ;; selected tags for feed
  [:all-activities nil])

(defquery top-blogs-query
  {:venia/operation {:operation/type :query
                     :operation/name "top_blogs"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:top_blogs {:vertical :$vertical}
                      [[:results [:id
                                  :title
                                  [:tags :fragment/tagFields]
                                  :creation_date
                                  :reading_time
                                  :upvote_count
                                  [:author_info [:name :image_url]]]]]]]})

(reg-query :top-blogs top-blogs-query)

(defn top-blogs [db]
  [:top-blogs {:vertical (:wh.db/vertical db)}])

(defquery recent-issues-query
  {:venia/operation {:operation/type :query
                     :operation/name "recent_issues"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:recent_issues {:vertical :$vertical}
                      [[:results [:id
                                  [:compensation [:amount]]
                                  :title
                                  :level
                                  [:repo [:primary_language]]
                                  [:company [:name :slug :logo]]]]]]]})
(reg-query :recent-issues recent-issues-query)
(defn recent-issues [db]
  [:recent-issues {:vertical (:wh.db/vertical db)}])

(defquery top-companies-queries
  {:venia/operation {:operation/type :query
                     :operation/name "top_companies"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:top_companies {:vertical :$vertical}
                      [[:results [:id
                                  :name
                                  :slug
                                  :logo
                                  :total_published_issue_count
                                  :total_published_job_count
                                  [:tags :fragment/tagFields]
                                  [:locations [:city :country]]]]]]]})
(reg-query :top-companies top-companies-queries)
(defn top-companies [db]
  [:top-companies {:vertical (:wh.db/vertical db)}])

(defquery top-users-queries
  {:venia/operation {:operation/type :query
                     :operation/name "top_users"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:top_users {:vertical :$vertical}
                      [[:results [:id
                                  :name
                                  :created
                                  :image_url
                                  :blog_count
                                  :issue_count]]]]]})
(reg-query :top-users top-users-queries)
(defn top-users [db]
  [:top-users {:vertical (:wh.db/vertical db)}])

(defquery recent-jobs-query
  {:venia/operation {:operation/type :query
                     :operation/name "recent_jobs"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:recent_jobs {:vertical :$vertical}
                      [[:results [:id
                                  :title
                                  :slug
                                  [:company_info [:id
                                                  :name
                                                  :slug
                                                  :logo
                                                  :total_published_job_count]]]]]]]})
(reg-query :recent-jobs recent-jobs-query)
(defn recent-jobs [db]
  [:recent-jobs {:vertical (:wh.db/vertical db)}])

(defquery top-tags-query
  {:venia/operation {:operation/type :query
                     :operation/name "top_tags"}
   :venia/variables [{:variable/name "vertical"
                      :variable/type :vertical}]
   :venia/queries   [[:top_tags {:vertical :$vertical}
                      [[:results :fragment/tagFields]]]]})
(reg-query :top-tags top-tags-query)
(defn top-tags [db]
  [:top-tags {:vertical (:wh.db/vertical db)}])

#?(:cljs
   (defmethod on-page-load :homepage-new [db]
     [(into [:graphql/query] (all-activities db))
      (into [:graphql/query] (top-blogs db))
      (into [:graphql/query] (top-companies db))
      (into [:graphql/query] (top-tags db))
      (into [:graphql/query] (top-users db))
      (into [:graphql/query] (recent-issues db))
      (into [:graphql/query] (recent-jobs db))]))