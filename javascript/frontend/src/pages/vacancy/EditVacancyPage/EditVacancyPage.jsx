import React, {useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './EditVacancyPage.css';
import {editEmployerVacancy, getEmployerVacancyDetails} from "../../../services/VacancyService.js";
import {currenciesMap, experienceLevelsMap, workTypesMap} from "../../../constants/Common.jsx";

const EditVacancyPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
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

    useEffect(() => {
        async function fetchVacancyData() {
            try {
                const response = await getEmployerVacancyDetails(id);
                setFormData({
                    title: response.title,
                    salaryMin: response.salaryMin?.amount || "",
                    salaryMax: response.salaryMax?.amount || "",
                    salaryCurrency: response.salaryMin?.currency || currenciesMap.BYN,
                    workType: response.workType,
                    location: response.location || "",
                    description: response.description || "",
                    experienceLevel: response.experienceLevel,
                });
            } catch (err) {
                setError('Не удалось загрузить информацию о вакансии');
            } finally {
                setLoading(false);
            }
        }

        fetchVacancyData();
    }, [id]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await editEmployerVacancy(id, formData);
            navigate(`/employer-vacancies/${id}`);
        } catch (error) {
            setError('Не удалось обновить вакансию');
        }
    };

    const handleCancel = () => {
        navigate(`/employer-vacancies/${id}`);
    };

    if (loading) {
        return <div className="edit-vacancy-container loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="edit-vacancy-container error">{error}</div>;
    }

    return (
        <div className="edit-vacancy-container">
            <h1>Редактировать вакансию</h1>
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
                                name="salaryCurrency"
                                value={formData.salaryCurrency}
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
                            <option key={key} value={key}>
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
                            <option key={key} value={key}>
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

                <div className="form-actions">
                    <button type="button" className="cancel-button" onClick={handleCancel}>
                        Отмена
                    </button>
                    <button type="submit" className="submit-button">
                        Сохранить изменения
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditVacancyPage; 