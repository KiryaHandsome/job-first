import React, {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {apiCall} from '../../../utils/api.js';
import './VacancyDetailsPage.css';
import {experienceLevelsMap, workTypesMap} from "../../../constants/Common.jsx";
import {applyToVacancy} from "../../../services/VacancyService.js";

export default function VacancyDetailsPage() {
    const {id} = useParams();
    const navigate = useNavigate();
    const [vacancy, setVacancy] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchVacancyDetails = async () => {
            try {
                const response = await apiCall(`com.job.first.vacancy.get_vacancy_by_id`, {
                    id: id,
                });
                setVacancy(response);
                setLoading(false);
            } catch (err) {
                setError('Не удалось загрузить информацию о вакансии');
                setLoading(false);
            }
        };

        fetchVacancyDetails();
    }, [id]);

    const formatSalary = (min, max) => {
        if (!min?.amount && !max?.amount) return 'По договоренности';
        if (!min?.amount) return `до ${max.amount.toLocaleString()} ${max.currency}`;
        if (!max?.amount) return `от ${min.amount.toLocaleString()} ${min.currency}`;
        return `${min.amount.toLocaleString()} - ${max.amount.toLocaleString()} ${min.currency}`;
    };

    const formatDate = (timestamp) => {
        return new Date(timestamp).toLocaleDateString('ru-RU', {
            day: 'numeric',
            month: 'long',
            year: 'numeric'
        });
    };

    const handleBackClick = () => {
        navigate(-1);
    };

    const handleApplyClick = () => {
        const response = applyToVacancy(id);
        // todo: handle?
    }

    if (loading) {
        return <div className="vacancy-details-page loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="vacancy-details-page error">{error}</div>;
    }

    if (!vacancy) {
        return <div className="vacancy-details-page error">Вакансия не найдена</div>;
    }

    return (
        <div className="vacancy-details-page">
            <button className="details-back-button" onClick={handleBackClick}>
                ← Назад к списку
            </button>

            <div className="details-header">
                <h1 className="details-title">{vacancy.title}</h1>
                <div className="details-salary">{formatSalary(vacancy.salaryMin, vacancy.salaryMax)}</div>
            </div>

            <div className="details-info">
                <div className="details-section">
                    <h2>Основная информация</h2>
                    <div className="details-grid">
                        <div className="details-item">
                            <span className="details-label">Тип работы:</span>
                            <span className="details-value">{workTypesMap[vacancy.workType]}
                            </span>
                        </div>
                        <div className="details-item">
                            <span className="details-label">Опыт:</span>
                            <span className="details-value">{experienceLevelsMap[vacancy.experienceLevel]}
                            </span>
                        </div>
                        <div className="details-item">
                            <span className="details-label">Локация:</span>
                            <span className="details-value">{vacancy.location || 'Не указана'}</span>
                        </div>
                        <div className="details-item">
                            <span className="details-label">Откликов:</span>
                            <span className="details-value">{vacancy.appliesCount || 0}</span>
                        </div>
                    </div>
                </div>

                <div className="details-section">
                    <h2>Описание</h2>
                    <p className="details-description">{vacancy.description}</p>
                </div>

                <div className="details-section">
                    <h2>Дополнительная информация</h2>
                    <div className="details-grid">
                        <div className="details-item">
                            <span className="details-label">Дата публикации:</span>
                            <span className="details-value">{formatDate(vacancy.createdAtMillis)}</span>
                        </div>
                        <div className="details-item">
                            <span className="details-label">Дата обновления:</span>
                            <span className="details-value">{formatDate(vacancy.editedAtMillis)}</span>
                        </div>
                    </div>
                </div>
            </div>

            <div className="details-actions">
                <button
                    className={`details-respond-button ${vacancy.userApplied ? 'applied' : ''}`}
                    disabled={vacancy.userApplied}
                    onClick={handleApplyClick   }
                >
                    {vacancy.userApplied ? 'Вы уже откликнулись' : 'Откликнуться на вакансию'}
                </button>
            </div>
        </div>
    );
}