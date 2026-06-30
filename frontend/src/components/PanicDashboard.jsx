import React, { useState, useEffect } from 'react';
import './PanicDashboard.css';

function PanicDashboard() {
  const [taskInput, setTaskInput] = useState('');
  const [deadline, setDeadline] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [sprints, setSprints] = useState([]);
  const [error, setError] = useState(null);
  
  const [activeSprintId, setActiveSprintId] = useState(null);
  const [timeLeft, setTimeLeft] = useState(0);

  useEffect(() => {
    let timer;
    if (activeSprintId !== null && timeLeft > 0) {
      timer = setInterval(() => setTimeLeft((prev) => prev - 1), 1000);
    } else if (timeLeft === 0) {
      setActiveSprintId(null);
    }
    return () => clearInterval(timer);
  }, [activeSprintId, timeLeft]);

  const handleStartStop = (sprint) => {
    if (activeSprintId === sprint.sequence) {
      setActiveSprintId(null);
    } else {
      setActiveSprintId(sprint.sequence);
      setTimeLeft((sprint.durationMins || 15) * 60);
    }
  };

  const handlePanicSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('https://panic-button-backend-bito.onrender.com', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: taskInput, deadline: deadline })
      });
      const data = await response.json();
      setSprints(data);
    } catch (err) {
      setError("Failed to connect to backend.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>The Last-Minute Life Saver</h1>
      </header>

      <form onSubmit={handlePanicSubmit} className="panic-form">
  <input 
    type="text" 
    placeholder="What massive task are you facing?" 
    value={taskInput} 
    onChange={(e) => setTaskInput(e.target.value)} 
    required 
  />
  <input 
    type="datetime-local" 
    value={deadline} 
    onChange={(e) => setDeadline(e.target.value)} 
    required 
  />
  <button type="submit" className="panic-button">
    {isLoading ? "ANALYZING..." : "HIT THE PANIC BUTTON"}
  </button>
</form>
      {error && <div className="error-message">{error}</div>}

      <div className="sprint-list">
        {sprints.map((sprint) => (
          <div key={sprint.sequence} className="sprint-card">
            <h3>{sprint.title}</h3>
            <span className="duration">
              {activeSprintId === sprint.sequence 
                ? `⏱ ${Math.floor(timeLeft / 60)}:${(timeLeft % 60).toString().padStart(2, '0')}` 
                : `⏱ ${sprint.durationMins || 15} mins`}
            </span>
            <button 
              className={`start-btn ${activeSprintId === sprint.sequence ? 'stop' : ''}`}
              onClick={() => handleStartStop(sprint)}
            >
              {activeSprintId === sprint.sequence ? "Stop" : "Start"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
export default PanicDashboard;