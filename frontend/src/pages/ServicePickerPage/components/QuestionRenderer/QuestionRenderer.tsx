import React from "react";
import HierarchicalSelector from "../HierarchicalSelector/HierarchicalSelector";
import VacanciesNumber from "../VacanciesNumber/VacanciesNumber";
import SingleChoice from "../SingleChoice/SingleChoice";
import MultipleChoice from "../MultipleChoice/MultipleChoice";

interface Question {
  referenceType?: any;
  id: number;
  type: string;
  questionText: string;
  inputType?: string;
  placeholder?: string;
  validation?: { min: number; max: number };
  options?: { id: number; text: string }[];
  isRequired: boolean;
}

interface Props {
  question: Question;
  answer: any;
  onChange: (value: any) => void;
  error: string | null;
  collection?: any;
}

const QuestionRenderer: React.FC<Props> = ({ question, answer, onChange, error, collection }) => {
  switch (question.type) {
    case "reference":
      return (
        <HierarchicalSelector
          title={question.questionText}
          collection={collection || []}
          selectedItems={answer || []}
          onItemsChange={onChange}
          loading={!collection}
          error={error}
          dataQa={`${question.referenceType}-selector`}
        />
      );
      case "input":
        return (
          <VacanciesNumber
            onNumberChange={(value) => onChange(value)}
            error={error}
            question={question}
            placeholder={question.placeholder} 
          />
        );
    case "single-choice":
      return (
        <SingleChoice
          question={question}
          selectedOption={answer}
          onSelect={onChange}
          error={error}
        />
      );
    case "multiple-choice":
      return (
        <MultipleChoice
          question={question}
          selectedOptions={answer || []}
          onToggle={onChange}
          error={error}
        />
      );
    default:
      return null;
  }
};

export default QuestionRenderer;