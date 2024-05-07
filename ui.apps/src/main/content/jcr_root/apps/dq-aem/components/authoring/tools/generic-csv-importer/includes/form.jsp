<form id="form">
    <div class="form-row">
            <h4 acs-coral-heading class="coral-Heading coral-Heading--4">CSV File</h4>
            <span>
                <input
                        accept="*/*"
                        type="file"
                        name="csv"
                        ngf-select
                        required
                        placeholder="Select the Categories CSV file"/>
            </span>
    </div>
    <div class="form-row">
        <h4 acs-coral-heading class="coral-Heading coral-Heading--4">Content Fragment Parent Path</h4>
        <span>
            <input type="text"
                   name="parentPath"
                   class="coral-Textfield"
                   required="true"
                   placeholder=""
            value="/content/dam/dq-aem/mixed_cf_folder"/>
        </span>
    </div>
    <div class="form-row">
        <h4 acs-coral-heading class="coral-Heading coral-Heading--4">Model Type</h4>
        <span>
            <select required="true" name="modelType" class="coral-Textfield">

                <option value="csv_model">CSV Model</option>
                <option value="list_model">List Model</option>
                <option value="data_time_model">Date time Model</option>
            </select>
        </span>
    </div>
    <div class="form-row">
        <div class="form-left-cell">&nbsp;</div>
        <button onclick="createContentFragment();return false;" class="coral-Button coral-Button--primary">Submit</button>
    </div>
</form>




