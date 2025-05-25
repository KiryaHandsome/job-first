import {useState} from 'react';
import './VacancyFilters.css';
import {workTypesMap, experienceLevelsMap} from "../../../../constants/Common.jsx";


export function VacancyFilters({filters, onFilterChange}) {
    const [localFilters, setLocalFilters] = useState(filters);

    const handleChange = (e) => {
        const {name, value, type, checked} = e.target;
        const newValue = type === 'checkbox' ? checked : value;

        setLocalFilters(prev => ({
            ...prev,
            [name]: newValue === "" ? null : newValue
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
                    placeholder="Поиск по вакансиям..."
                />
            </div>

            <div className="filter-group">
                <label>Зарплата</label>
                <div className="salary-range">
                    <input
                        type="number"
                        name="salaryFrom"
                        value={localFilters.salaryFrom}
                        onChange={handleChange}
                        placeholder="От"
                    />
                    <select
                        name="currency"
                        value={localFilters.currency}
                        onChange={handleChange}
                        disabled={true}
                    >
                        <option value="BYN">BYN</option>
                    </select>
                </div>
            </div>

            <div className="filter-group">
                <label>Опыт</label>
                <select
                    name="experience"
                    value={localFilters.experience}
                    onChange={handleChange}
                >
                    <option value="">Любой</option>
                    <option value="NO_EXPERIENCE">{experienceLevelsMap.NO_EXPERIENCE}</option>
                    <option value="ONE_YEAR">{experienceLevelsMap.ONE_YEAR}</option>
                    <option value="THREE_YEARS">{experienceLevelsMap.THREE_YEARS}</option>
                    <option value="SIX_PLUS_YEARS">{experienceLevelsMap.SIX_PLUS_YEARS}</option>
                </select>
            </div>

            <div className="filter-group">
                <label>Формат работы</label>
                <select
                    name="workType"
                    value={localFilters.employmentType}
                    onChange={handleChange}
                >
                    <option value="">Любой</option>
                    <option value="OFFICE">{workTypesMap.OFFICE}</option>
                    <option value="REMOTE">{workTypesMap.REMOTE}</option>
                    <option value="HYBRID">{workTypesMap.HYBRID}</option>
                </select>
            </div>

            <button type="submit" className="apply-filters">
                Найти вакансии
            </button>
        </form>
    );
}
