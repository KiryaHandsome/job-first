import React, {useState} from 'react';
import './CreateVacancyPage.css';
import {createVacancy} from "../../../services/VacancyService.js";
import {currenciesMap, experienceLevelsMap, workTypesMap} from "../../../constants/Common.jsx";


const CreateVacancyPage = () => {
    const [formData, setFormData] = useState({
        title: '',
        salaryMin: "",
        salaryMax: "",
        salaryCurrency: currenciesMap.BYN,
        workType: null,
        location: null,
        description: null,
        experienceLevel: null,
    });

    const handleChange = (e) => {
        const {name, value} = e.target;

        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        createVacancy(formData)
        console.log('Form submitted:', formData);
    };

    return (
        <div className="create-vacancy-container">
            <h1>Создать новую вакансию</h1>
            <form onSubmit={handleSubmit} className="vacancy-form">
                <div className="form-group">
                    <label htmlFor="title">Заголовок вакансии</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                        placeholder="Введите заголовок вакансии..."
                    />
                </div>

                <div className="form-group">
                    <label>Зарплата</label>
                    <div className="salary-fields">
                        <div className="salary-input-group">
                            <input
                                type="number"
                                name="salaryMin"
                                value={formData.salaryMin}
                                onChange={handleChange}
                                placeholder="От ..."
                                min="0"
                            />
                            <input
                                type="number"
                                name="salaryMax"
                                value={formData.salaryMax}
                                onChange={handleChange}
                                placeholder="До ..."
                                min="0"
                            />
                        </div>
                        <div className="currency-selector">
                            <select
                                name="currency"
                                value={formData.currency}
                                onChange={handleChange}
                            >
                                {Object.entries(currenciesMap).map(([key, value]) => (
                                    <option key={key} value={key}>{value}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="workType">Формат работы</label>
                    <select
                        id="workType"
                        name="workType"
                        value={formData.workType}
                        onChange={handleChange}
                        required
                    >
                        {Object.entries(workTypesMap).map(([key, value]) => (
                            <option key={value} value={key}>
                                {value}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="location">Местоположение</label>
                    <input
                        type="text"
                        id="location"
                        name="location"
                        value={formData.location}
                        onChange={handleChange}
                        placeholder="Введите местоположение работы..."
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="experienceLevel">Опыт работы</label>
                    <select
                        id="experienceLevel"
                        name="experienceLevel"
                        value={formData.experienceLevel}
                        onChange={handleChange}
                        required
                    >
                        {Object.entries(experienceLevelsMap).map(([key, value]) => (
                            <option key={value} value={key}>
                                {value}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="description">Детальное описание работы</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Введите детальное описание работы..."
                        rows="5"
                    />
                </div>

                <button type="submit" className="submit-button">
                    Создать вакансию
                </button>
            </form>
        </div>
    );
};

export default CreateVacancyPage;
