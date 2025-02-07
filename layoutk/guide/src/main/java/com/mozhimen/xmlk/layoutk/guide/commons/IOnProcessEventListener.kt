package com.mozhimen.xmlk.layoutk.guide.commons

/**
 * Listener to notify the state of Spotlight.
 */
interface IOnProcessEventListener {

  /**
   * Called when Spotlight is started
   */
  fun onStart()

  /**
   * Called when Spotlight is ended
   */
  fun onEnd()
}
