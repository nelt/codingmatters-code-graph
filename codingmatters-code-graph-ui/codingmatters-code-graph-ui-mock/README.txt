______
VISION

The objective is to have a static by "link functional" mockup. This should enable
the articulation between (1) UI design and (2) implementation.

(1) The mockup is browsable (provided it is served typically by an apache server).
This way, the UI design can be completely decoupled from its implementation

(2) UI implementation takes the mockup files as unit test input

________________________
ENABLING MOCKUP BROWSING

Just put the following apache directive to an enabled apache site (while replacing 
with your real paths).

        Alias /codingmatters-mockup/code-graph  "/<path to project>/codingmatters-code-graph/codingmatters-code-graph-ui/codingmatters-code-graph-ui-mock/src/main/resources"

        <Directory "/<path to project>/codingmatters-code-graph/codingmatters-code-graph-ui/codingmatters-code-graph-ui-mock/src/main/resources">
            Options Indexes MultiViews
            AllowOverride None
            Order allow,deny
            Allow from all
        </Directory>
        
_________
BRANCHING

UI work should be done on a specific branch so that unit tests remains stable. The 
merge should occur when a coherent set of changes have been made to the UI. Thus the
UI implementation will be done on a stable and coherent set of resources.