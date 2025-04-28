import { useState } from 'react';
import './VacancyFilters.css';

export function VacancyFilters({ filters, onFilterChange }) {
    const [localFilters, setLocalFilters] = useState(filters);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        const newValue = type === 'checkbox' ? checked : value;
        
        setLocalFilters(prev => ({
            ...prev,
            [name]: newValue
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onFilterChange(localFilters);
    };

    return (
        <form onSubmit={handleSubmit} className="filters-form">
            <div className="filter-group">
                <input
                    type="text"
                    name="search"
                    value={localFilters.search}
                    onChange={handleChange}
                    placeholder="Search vacancies..."
                />
            </div>

            <div className="filter-group">
                <label>Salary Range</label>
                <div className="salary-range">
                    <input
                        type="number"
                        name="salaryFrom"
                        value={localFilters.salaryFrom}
                        onChange={handleChange}
                        placeholder="From"
                    />
                    <input
                        type="number"
                        name="salaryTo"
                        value={localFilters.salaryTo}
                        onChange={handleChange}
                        placeholder="To"
                    />
                </div>
            </div>

            <div className="filter-group">
                <label>Experience</label>
                <select
                    name="experience"
                    value={localFilters.experience}
                    onChange={handleChange}
                >
                    <option value="">Any</option>
                    <option value="no_experience">No Experience</option>
                    <option value="1-3">1-3 years</option>
                    <option value="3-5">3-5 years</option>
                    <option value="5+">5+ years</option>
                </select>
            </div>

            <div className="filter-group">
                <label>Employment Type</label>
                <select
                    name="employmentType"
                    value={localFilters.employmentType}
                    onChange={handleChange}
                >
                    <option value="">Any</option>
                    <option value="full_time">Full Time</option>
                    <option value="part_time">Part Time</option>
                    <option value="contract">Contract</option>
                </select>
            </div>

            <div className="filter-group">
                <label>
                    <input
                        type="checkbox"
                        name="remote"
                        checked={localFilters.remote}
                        onChange={handleChange}
                    />
                    Remote Only
                </label>
            </div>

            <button type="submit" className="apply-filters">
                Apply Filters
            </button>
        </form>
    );
}
