/**
 * Copyright 2017- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.lx.parameter;

import com.google.gson.JsonObject;

import heronarts.lx.LX;

public class LXTriggerModulation extends LXParameterModulation {

  public final BooleanParameter source;
  public final BooleanParameter target;

  private final boolean sourceMomentary;
  private final boolean targetMomentary;

  public LXTriggerModulation(BooleanParameter source, BooleanParameter target) {
    super(source, target);
    this.source = source;
    this.target = target;
    this.sourceMomentary = (source.getMode() == BooleanParameter.Mode.MOMENTARY);
    this.targetMomentary = (target.getMode() == BooleanParameter.Mode.MOMENTARY);
    this.source.addListener(this);
  }

  public LXTriggerModulation(LX lx, JsonObject obj) {
    this(
      (BooleanParameter) getParameter(lx, obj.getAsJsonObject(KEY_SOURCE)),
      (BooleanParameter) getParameter(lx, obj.getAsJsonObject(KEY_TARGET))
    );
  }

  @Override
  public void onParameterChanged(LXParameter p) {
    super.onParameterChanged(p);
    if (this.enabled.isOn()) {
      if (p == this.source) {
        if (this.sourceMomentary) {
          if (this.source.isOn()) {
            if (this.targetMomentary) {
              this.target.setValue(true);
            } else {
              this.target.toggle();
            }
          }
        } else {
          this.target.setValue(this.source.getValue());
        }
      }
    }
  }

  @Override
  public void dispose() {
    this.source.removeListener(this);
    super.dispose();
  }
}
