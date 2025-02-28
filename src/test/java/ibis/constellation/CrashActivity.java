/*
 * Copyright 2018 Netherlands eScience Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ibis.constellation;

/**
 * @version 1.0
 * @since 1.0
 *
 */
public class CrashActivity extends Activity {

    private static final long serialVersionUID = -4021583343422065387L;

    private boolean crashInitialize;
    private boolean crashProcess;
    private boolean crashCleanup;

    private static class RTE extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public RTE(String string) {
            super(string);
        }
    }

    public CrashActivity(AbstractContext c, boolean crashInitialize, boolean crashProcess, boolean crashCleanup) {
        super(c, true);
        this.crashInitialize = crashInitialize;
        this.crashProcess = crashProcess;
        this.crashCleanup = crashCleanup;
    }

    @Override
    public void setIdentifier(ActivityIdentifier id) {
        super.setIdentifier(id);
    }

    @Override
    public int initialize(Constellation constellation) {
        if (crashInitialize) {
            throw new RTE("Boom!");
        }
        return SUSPEND;
    }

    @Override
    public int process(Constellation constellation, Event event) {
        if (crashProcess) {
            throw new RTE("Boom!");
        }
        return FINISH;
    }

    @Override
    public void cleanup(Constellation constellation) {
        if (crashCleanup) {
            throw new RTE("Boom!");
        }
    }
}
