// Copyright 2011 Software Freedom Conservancy. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

goog.provide('webdriver.Session');

goog.require('webdriver.Capabilities');



/**
 * Contains information about a WebDriver session.
 * @param {string} id The session ID.
 * @param {!(Object|webdriver.Capabilities)} capabilities The session
 *     capabilities.
 * @constructor
 */
webdriver.Session = function(id, capabilities) {

  /** @private {string} */
  this.id_ = id;

  /** @private {!webdriver.Capabilities} */
  this.caps_ = new webdriver.Capabilities().merge(capabilities);
};


/**
 * Returns a list of the currently active sessions.
 * @param {!webdriver.CommandExecutor} executor
 * @return {!webdriver.promise.Promise} A promise that will be resolved with an
 *     array of sessions.
 */
webdriver.Session.getSessions = function(executor) {
  var command = new webdriver.Command(webdriver.CommandName.GET_SESSIONS);
  var fn = goog.bind(executor.execute, executor, command);
  return webdriver.promise.controlFlow().execute(function() {
    return webdriver.promise.checkedNodeCall(fn).then(function(response) {
      bot.response.checkResponse(response);
      var sessions = [];
      for (var session in response['value']) {
        session = response['value'][session];
        sessions.push(new webdriver.Session(session['id'], session['capabilities']));
      }
      return sessions;
    });
  }, 'Session.getSessions()');
};


/**
 * @return {string} This session's ID.
 */
webdriver.Session.prototype.getId = function() {
  return this.id_;
};


/**
 * @return {!webdriver.Capabilities} This session's capabilities.
 */
webdriver.Session.prototype.getCapabilities = function() {
  return this.caps_;
};


/**
 * Retrieves the value of a specific capability.
 * @param {string} key The capability to retrieve.
 * @return {*} The capability value.
 */
webdriver.Session.prototype.getCapability = function(key) {
  return this.caps_.get(key);
};


/**
 * Returns the JSON representation of this object, which is just the string
 * session ID.
 * @return {string} The JSON representation of this Session.
 */
webdriver.Session.prototype.toJSON = function() {
  return this.getId();
};
