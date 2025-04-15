// ModelTests.js
import React from 'react';
import Layout from './Layout'; 
import './styles.css';         


// 임의로 설정되어 있는데, db 연동해야함함
function ModelTests() {
  return (
    <Layout>
      <h1>Model Tests</h1>
      <div className="card">
        <div className="card-header">Test Results</div>
        <div className="card-body">
          <p>Here is a summary of the recent model tests.</p>
          <table className="table">
            <thead>
              <tr>
                <th>Model Name</th>
                <th>Flagged Events</th>
                <th>View Details</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Model 1</td>
                <td>5</td>
                <td><button className="button">Details</button></td>
              </tr>
              <tr>
                <td>Model 2</td>
                <td>53</td>
                <td><button className="button">Details</button></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  );
}

export default ModelTests;
