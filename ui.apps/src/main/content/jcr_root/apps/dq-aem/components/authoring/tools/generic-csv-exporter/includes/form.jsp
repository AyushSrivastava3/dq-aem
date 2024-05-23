<form id="form">

    <div class="form-row">
        <h4 acs-coral-heading class="coral-Heading coral-Heading--4">Content Fragment Path</h4>
        <span>
            <input type="text"
                   name="parentPath"
                   class="coral-Textfield"
                   required="true"
                   placeholder=""
                   value="/content/dam/dq-aem/task4"/>
        </span>
    </div>
    <div class="form-row">
        <h4 acs-coral-heading class="coral-Heading coral-Heading--4">Csv Creation Path</h4>
        <span>
            <input type="text"
                   name="csvPath"
                   class="coral-Textfield"
                   required="true"
                   placeholder=""
                   value="/Users/dq-mac-m2-1/Documents/csv_folder1/"/>
        </span>
    </div>

    <div class="form-row">
        <div class="form-left-cell">&nbsp;</div>
        <button onclick="createCSV();return false;" class="coral-Button coral-Button--primary">Create CSV</button>
    </div>
</form>
