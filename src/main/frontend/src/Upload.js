// Upload.js
import React, { useState } from 'react';
import Layout from './Layout';
import './styles.css';

function Upload() {
  const [endpoint, setEndpoint] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('API Endpoint submitted:', endpoint);
    // 추가 로직 구현 가능
  };

  return (
    <Layout>
      <h1>Upload Page</h1>
      <p className="upload-description">
        Enter the API endpoint for your deployed LLM model below:
      </p> {/* p 태그 아래에 간격을 주기 위해 className 추가 */}

      <form onSubmit={handleSubmit} className="upload-form">
        <label htmlFor="endpoint" className="form-label">
          API Endpoint URL:
        </label> {/* 라벨과 인풋 사이에 간격을 주기 위해 block 요소로 변경 */}
        <input
          type="url"
          id="endpoint"
          name="endpoint"
          className="form-input"
          placeholder="http://your-model-api.com/infer"
          value={endpoint}
          onChange={(e) => setEndpoint(e.target.value)}
          required
        />
        <button type="submit" className="button">
          Submit
        </button>
      </form>

      <div className="upload-instructions">
        <h2>Instructions</h2>
        <p>
          Ensure your LLM model API endpoint is correctly deployed and accessible.
          You can test your endpoint using tools like <strong>Mindgard</strong> to detect potential vulnerabilities.
        </p>
        <p>
          <code>
            <br/>
            Example:mindgard test example-model --url http://127.0.0.1/infer --system-prompt "$(cat system-prompt.txt)"
          </code>
        </p>
        <p>
          After submission, the system will process your endpoint and provide further instructions or results.
        </p>
      </div>
    </Layout>
  );
}

export default Upload;
